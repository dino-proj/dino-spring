// Copyright 2021 dinospring.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.modules.oss;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drew.imaging.FileTypeDetector;
import com.drew.imaging.ImageMetadataReader;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.data.FileMeta;
import org.dinospring.commons.data.FileTypes;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.modules.oss.UploadMeta.OssType;
import org.dinospring.core.modules.oss.config.OssModuleProperties;
import org.dinospring.core.sys.token.TokenService;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

/**
 *
 * @author tuuboo
 */

public interface OssControllerBase {
  /**
   * Logger
   * @return
   */
  Logger log();

  /**
   * 获取Oss配置属性
   * @return
   */
  default OssModuleProperties ossModuleProperties() {
    return ContextHelper.findBean(OssModuleProperties.class);
  }

  /**
   * 获取OssService
   * @return
   */
  default OssService ossService() {
    return ContextHelper.findBean(OssService.class);
  }

  /**
   * 获取Token服务
   * @return
   */
  private TokenService tokenService() {
    return ContextHelper.findBean(TokenService.class);
  }

  /**
   * 上传文件
   * @param tenantId
   * @param fileType
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "上传文件到Tmp")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
  @Parameter(in = ParameterIn.PATH, name = "file_type", description = "文件类型", required = true)
  @PostMapping(value = "/upload/{file_type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<FileMeta> uploadFile(@PathVariable("tenant_id") String tenantId,
      @PathVariable("file_type") FileTypes fileType, String service, MultipartFile file) throws IOException {

    var fileMeta = new FileMeta();
    fileMeta.setSize(file.getSize());
    var contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(file.getOriginalFilename());

    var data = new ByteArrayInputStream(file.getBytes());
    var input = new BufferedInputStream(data);

    var type = FileTypeDetector.detectFileType(input);
    var reader = ImageMetadataReader.readMetadata(data, file.getSize());
    for (var dir : reader.getDirectories()) {

    }

    var fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
    var objectName = FilenameUtils.concat(tenantId, fileName);

    fileMeta.setPath(fileName);
    try {
      ossService().putObject(file.getInputStream(), "tmp", objectName, fileType.contextType);

      return Response.success(fileMeta);
    } catch (IOException e) {
      log().error("Error occured while upload file[{}]", fileName, e);
      return Response.fail(Status.CODE.FAIL_EXCEPTION);
    }

  }

  /**
   * 获取文件路径
   * @param tenantId
   * @param fileName
   * @param resp
   * @throws IOException
   */
  @Operation(summary = "获取文件")
  @ParamTenant
  @GetMapping("/file/{file_path}")
  default void get(@PathVariable("tenant_id") String tenantId, @PathVariable("file_path") String filePath, String token,
      @RequestParam("_nonce") String nonce, HttpServletResponse resp) throws IOException {

    var contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(filePath);
    resp.setContentType(StringUtils.defaultString(contentType, "application/x-download"));

    var objectName = FilenameUtils.concat(tenantId, fileName);

    ossService().trasferObject("tmp", objectName, resp.getOutputStream());
  }

  /**
   * 获取上传元信息
   * @param tenantId
   * @param fileType
   * @param service
   * @param request
   * @return
   */
  @Operation(summary = "获取文件上传信息")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
  @Parameter(name = "file_type", required = true, description = "文件类型")
  @GetMapping("/upload_meta")
  default Response<UploadMeta> requestUpload(@PathVariable("tenant_id") String tenantId,
      @RequestParam("file_type") FileTypes fileType, @RequestParam String service, HttpServletRequest request) {

    var tenant = ContextHelper.currentTenant();
    var meta = new UploadMeta();
    meta.setOssType(OssType.LOCAL);
    meta.setMethod(UploadMeta.Method.POST);

    var nonce = String.valueOf(System.nanoTime());

    Map<String, String> params = Map.of("service", service, "tenant", tenantId, "_nonce", nonce);

    var token = tokenService().siginParams(tenant.getSecretKey(), params);

    meta.setUploadUrl(ossModuleProperties().getOssUrlBase() + "/v1/" + tenantId + "/oss/upload/" + fileType.toString()
        + "?_nonce=" + nonce + "&token=" + token);
    return Response.success(meta);
  }

  default String toPath(FileMeta meta, String tenantId) {
    return "";

  }

}
