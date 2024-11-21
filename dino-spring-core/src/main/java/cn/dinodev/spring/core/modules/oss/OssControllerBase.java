// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.oss;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.data.AudioFileMeta;
import cn.dinodev.spring.commons.data.DocumentFileMeta;
import cn.dinodev.spring.commons.data.FileMeta;
import cn.dinodev.spring.commons.data.FileTypes;
import cn.dinodev.spring.commons.data.ImageFileMeta;
import cn.dinodev.spring.commons.data.VideoFileMeta;
import cn.dinodev.spring.commons.exception.BusinessException;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.core.annotion.param.ParamTenant;
import cn.dinodev.spring.core.modules.oss.UploadMeta.OssType;
import cn.dinodev.spring.core.modules.oss.config.OssModuleProperties;
import cn.dinodev.spring.core.sys.token.TokenService;
import cn.dinodev.spring.core.utils.MultiMediaUtils;
import cn.dinodev.spring.core.utils.MultiMediaUtils.MediaInfo;
import cn.dinodev.spring.data.domain.IdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.activation.FileTypeMap;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author Cody Lu
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
  default TokenService tokenService() {
    return ContextHelper.findBean(TokenService.class);
  }

  /**
  * 获取uuid服务
  * @return
  */
  default IdService idService() {
    return ContextHelper.findBean(IdService.class);
  }

  /**
   * 上传文件
   * @param tenant
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "上传文件到Tmp")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
  @PostMapping(value = "/upload/FILE", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<FileMeta> uploadFile(Tenant tenant,
      String service, MultipartFile file) throws IOException {

    var ref = new FileMeta[1];

    saveFile(tenant, service, file, media -> {

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
   * 上传音频
   * @param tenant
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "上传音频")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
  @PostMapping(value = "/upload/AUDIO", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<AudioFileMeta> uploadAudio(Tenant tenant, String service,
      MultipartFile file) throws IOException {

    var meta = new AudioFileMeta();

    saveFile(tenant, service, file, media -> {
      Assert.notNull(media, Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid audio"));
      Assert.isTrue(media.isAudio() && media.getDuration() != null,
          Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid audio"));

      meta.setFormat(media.getTypeName());
      meta.setDuration(media.getDuration());
      return meta;
    });

    return Response.success(meta);
  }

  /**
   * 上传视频
   * @param tenant
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "上传视频")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
  @PostMapping(value = "/upload/VIDEO", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<VideoFileMeta> uploadVideo(Tenant tenant, String service,
      MultipartFile file) throws IOException {

    var meta = new VideoFileMeta();

    saveFile(tenant, service, file, media -> {

      Assert.notNull(media, Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid video"));
      Assert.isTrue(media.isVideo() && media.getWidth() != null,
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
   * 上传图片
   * @param tenant
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "上传图片")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
  @PostMapping(value = "/upload/IMAGE", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<ImageFileMeta> uploadImage(Tenant tenant, String service,
      MultipartFile file) throws IOException {

    var meta = new ImageFileMeta();

    saveFile(tenant, service, file, media -> {

      Assert.notNull(media, Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid image"));
      Assert.isTrue(media.isImage() && media.getWidth() != null,
          Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid image"));

      meta.setWidth(media.getWidth());
      meta.setHeight(media.getHeight());
      meta.setFormat(media.getTypeName());
      return meta;
    });

    return Response.success(meta);
  }

  /**
   * 保存文件
   * @param tenant
   * @param serviceId
   * @param file
   * @param metaProvider
   * @throws IOException
   */
  private void saveFile(Tenant tenant, String serviceId, MultipartFile file,
      Function<MediaInfo, FileMeta> metaProvider) throws IOException {

    var media = MultiMediaUtils.extractMediaInfo(file);
    var meta = metaProvider.apply(media);

    meta.setSize(file.getSize());

    var objectName = new StringBuilder();
    if (Objects.nonNull(tenant)) {
      objectName.append(tenant.getId()).append('/');
    }
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
   * 获取上传元信息
   * @param tenant
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
  default Response<UploadMeta> requestUpload(Tenant tenant,
      @RequestParam("file_type") FileTypes fileType, @RequestParam String service, HttpServletRequest request) {

    var meta = new UploadMeta();
    meta.setOssType(OssType.LOCAL);
    meta.setMethod(UploadMeta.Method.POST);

    var nonce = String.valueOf(System.nanoTime());

    var tenantId = tenant.getId();

    Map<String, String> params = Map.of("service", service, "tenant", tenantId, "_nonce", nonce);

    var token = tokenService().siginParams(tenant.getSecretKey(), params);

    meta.setUploadUrl(ossModuleProperties().getOssUrlBase() + "/v1/" + tenantId + "/oss/upload/" + fileType.toString()
        + "?_nonce=" + nonce + "&token=" + token);
    return Response.success(meta);
  }

}
