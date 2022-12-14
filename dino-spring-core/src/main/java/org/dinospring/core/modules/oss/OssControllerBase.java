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

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.data.AudioFileMeta;
import org.dinospring.commons.data.DocumentFileMeta;
import org.dinospring.commons.data.FileMeta;
import org.dinospring.commons.data.FileTypes;
import org.dinospring.commons.data.ImageFileMeta;
import org.dinospring.commons.data.VideoFileMeta;
import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.modules.oss.UploadMeta.OssType;
import org.dinospring.core.modules.oss.config.OssModuleProperties;
import org.dinospring.core.sys.token.TokenService;
import org.dinospring.core.utils.MultiMediaUtils;
import org.dinospring.core.utils.MultiMediaUtils.MediaInfo;
import org.dinospring.data.domain.IdService;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

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
   * ??????Oss????????????
   * @return
   */
  default OssModuleProperties ossModuleProperties() {
    return ContextHelper.findBean(OssModuleProperties.class);
  }

  /**
   * ??????OssService
   * @return
   */
  default OssService ossService() {
    return ContextHelper.findBean(OssService.class);
  }

  /**
   * ??????Token??????
   * @return
   */
  default TokenService tokenService() {
    return ContextHelper.findBean(TokenService.class);
  }

  /**
  * ??????uuid??????
  * @return
  */
  default IdService idService() {
    return ContextHelper.findBean(IdService.class);
  }

  /**
   * ????????????
   * @param tenantId
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "???????????????Tmp")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "????????????")
  @PostMapping(value = "/upload/FILE", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<FileMeta> uploadFile(@PathVariable("tenant_id") String tenantId,
      String service, MultipartFile file) throws IOException {

    var ref = new FileMeta[1];

    saveFile(tenantId, service, file, media -> {

      if (media == null || !media.isMultiMedia()) {
        var meta = new DocumentFileMeta();
        ref[0] = meta;
      } else if (media.isImage()) {
        Assert.isTrue(media.getWidth() != null, Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid image"));

        var imageMeta = new ImageFileMeta();
        imageMeta.setWidth(media.getWidth());
        imageMeta.setHeight(media.getHeight());
        imageMeta.setFormat(media.getTypeName());
        ref[0] = imageMeta;
      } else if (media.isAudio()) {
        Assert.isTrue(media.getDuration() != null, Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid audio"));

        var audioMeta = new AudioFileMeta();
        audioMeta.setFormat(media.getTypeName());
        audioMeta.setDuration(media.getDuration());
        ref[0] = audioMeta;
      } else if (media.isVideo()) {
        Assert.isTrue(media.getWidth() != null, Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid video"));

        var videoMeta = new VideoFileMeta();
        videoMeta.setWidth(media.getWidth());
        videoMeta.setHeight(media.getHeight());
        videoMeta.setFormat(media.getTypeName());
        videoMeta.setDuration(media.getDuration());
        videoMeta.setResolution(media.getResolution());
        ref[0] = videoMeta;
      }
      return ref[0];
    });

    return Response.success(ref[0]);
  }

  /**
   * ????????????
   * @param tenantId
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "????????????")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "????????????")
  @PostMapping(value = "/upload/AUDIO", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<AudioFileMeta> uploadAudio(@PathVariable("tenant_id") String tenantId, String service,
      MultipartFile file) throws IOException {

    var meta = new AudioFileMeta();

    saveFile(tenantId, service, file, media -> {

      Assert.isTrue(media != null && media.isAudio() && media.getDuration() != null,
          Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid audio"));

      meta.setFormat(media.getTypeName());
      meta.setDuration(media.getDuration());
      return meta;
    });

    return Response.success(meta);
  }

  /**
   * ????????????
   * @param tenantId
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "????????????")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "????????????")
  @PostMapping(value = "/upload/VIDEO", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<VideoFileMeta> uploadVideo(@PathVariable("tenant_id") String tenantId, String service,
      MultipartFile file) throws IOException {

    var meta = new VideoFileMeta();

    saveFile(tenantId, service, file, media -> {

      Assert.isTrue(media != null && media.isVideo() && media.getWidth() != null,
          Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid video"));

      meta.setWidth(media.getWidth());
      meta.setHeight(media.getHeight());
      meta.setFormat(media.getTypeName());
      meta.setDuration(media.getDuration());
      meta.setResolution(media.getResolution());
      return meta;
    });

    return Response.success(meta);
  }

  /**
   * ????????????
   * @param tenantId
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "????????????")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "????????????")
  @PostMapping(value = "/upload/IMAGE", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<ImageFileMeta> uploadImage(@PathVariable("tenant_id") String tenantId, String service,
      MultipartFile file) throws IOException {

    var meta = new ImageFileMeta();

    saveFile(tenantId, service, file, media -> {

      Assert.isTrue(media != null && media.isImage() && media.getWidth() != null,
          Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid image"));

      meta.setWidth(media.getWidth());
      meta.setHeight(media.getHeight());
      meta.setFormat(media.getTypeName());
      return meta;
    });

    return Response.success(meta);
  }

  /**
   * ????????????
   * @param tenantId
   * @param serviceId
   * @param file
   * @param metaProvider
   * @throws IOException
   */
  private void saveFile(String tenantId, String serviceId, MultipartFile file,
      Function<MediaInfo, FileMeta> metaProvider) throws IOException {

    var media = MultiMediaUtils.extractMediaInfo(file);
    var meta = metaProvider.apply(media);

    meta.setSize(file.getSize());

    var objectName = new StringBuilder();
    objectName.append(tenantId).append('/');
    objectName.append(idService().genUUID());
    objectName.append('.').append(FilenameUtils.getExtension(file.getOriginalFilename()));
    var objectKey = objectName.toString();
    
    meta.setBucket(serviceId);
    meta.setPath(objectKey);

    try (var input = file.getInputStream()) {
      var contentType = media != null && media.getMime() != null ? media.getMime()
          : FileTypeMap.getDefaultFileTypeMap().getContentType(file.getOriginalFilename());
      ossService().putObject(input, "tmp", objectKey, contentType);

    } catch (IOException e) {
      log().error("Error occured while upload file[{}]", objectKey, e);
      throw BusinessException.of(Status.CODE.FAIL_EXCEPTION);
    }
  }

  /**
   * ?????????????????????
   * @param tenantId
   * @param fileType
   * @param service
   * @param request
   * @return
   */
  @Operation(summary = "????????????????????????")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "????????????")
  @Parameter(name = "file_type", required = true, description = "????????????")
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

}
