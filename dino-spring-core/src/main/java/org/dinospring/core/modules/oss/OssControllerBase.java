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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.apache.commons.io.FilenameUtils;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.data.FileMeta;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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
   */
  @Operation(summary = "上传文件到Tmp")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
  @Parameter(in = ParameterIn.PATH, name = "file_type", description = "文件类型", required = true)
  @PostMapping(value = "/upload/{file_type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<FileMeta> uploadFile(@PathVariable("tenant_id") String tenantId,
                                        @PathVariable("file_type") FileType fileType, String service, MultipartFile file) {
    var fileMeta = new FileMeta();
    fileMeta.setSize(file.getSize());

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
  @Operation(summary = "获取tmp文件")
  @ParamTenant
  @GetMapping("/tmp/{file_path}")
  default void get(@PathVariable("tenant_id") String tenantId, @PathVariable("file_path") String fileName,
                   HttpServletResponse resp) throws IOException {

    resp.setContentType("application/x-download");

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
                                             @RequestParam("file_type") FileType fileType, @RequestParam String service, HttpServletRequest request) {
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

  public enum FileType {
    //文件
    FILE("application/octet-stream"),
    //图片
    IMAGE("image/jpeg"),
    //视频
    VIDEO("video/mp4");

    private String contextType;

    FileType(String contextType) {
      this.contextType = contextType;
    }
  }
}
