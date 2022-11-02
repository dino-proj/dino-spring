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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.messages.Part;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

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
   * @param tenantId
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "上传文件到Tmp")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
  @PostMapping(value = "/upload/FILE", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  default Response<FileMeta> uploadFile(@PathVariable("tenant_id") String tenantId,
                                        String service, MultipartFile file) throws IOException {

    var ref = new FileMeta[1];

    saveFile(tenantId, service, file, media -> fileMeta(media, ref));

    return Response.success(ref[0]);
  }

  /**
   * meta信息
   * @param media
   * @param ref
   * @return
   */
  private FileMeta fileMeta(MediaInfo media, FileMeta[] ref) {
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
  }

  /**
   * 上传音频
   * @param tenantId
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "上传音频")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
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
   * 上传视频
   * @param tenantId
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "上传视频")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
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
   * 上传图片
   * @param tenantId
   * @param service
   * @param file
   * @return
   * @throws IOException
   */
  @Operation(summary = "上传图片")
  @ParamTenant
  @Parameter(name = "service", required = false, description = "服务名字")
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
   * 保存文件
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

  /**
   * 根据文件分片数量获取上传地址
   * @param tenantId
   * @param fileName
   * @param chunkNum
   * @param contentType
   * @return
   */
  @Operation(summary = "根据文件分片数量获取上传地址")
  @ParamTenant
  @GetMapping("/chunks")
  default Response<MultipartUploadAsync> multipartUploadAsync(@PathVariable("tenant_id") String tenantId, @RequestParam("file_name") String fileName, @RequestParam("chunk_num") Integer chunkNum, @RequestParam("content_type") String contentType) {
    var multipartUploadAsyncRes = new MultipartUploadAsync();
    var bucketName = "tmp";
    var objectKey = tenantId +
      '/' +
      UUID.randomUUID().toString() +
      "_" +
      fileName;
    multipartUploadAsyncRes.setFileName(objectKey);

    Multimap<String, String> objectObjectHashMultimap = HashMultimap.create();
    objectObjectHashMultimap.put("Content-Type", contentType);
    CreateMultipartUploadResponse response = ossService().createMultipartUploadAsync(bucketName, null, objectKey, objectObjectHashMultimap, null);

    String uploadId = response.result().uploadId();
    multipartUploadAsyncRes.setUploadId(uploadId);

    List<MultipartUploadAsync.MultipartUploadChunk> chunks = new ArrayList<>();
    for (var i = 0; i < chunkNum; i++) {
      var chunk = new MultipartUploadAsync.MultipartUploadChunk();
      String uploadUrl = ossService().createUploadUrlAsync(bucketName, objectKey, i, uploadId);
      chunk.setPartNumber(i);
      chunk.setUploadUrl(uploadUrl);
      chunks.add(chunk);
    }
    multipartUploadAsyncRes.setChunks(chunks);

    return Response.success(multipartUploadAsyncRes);
  }


  /**
   * 分片上传完后合并
   * @param tenantId
   * @param fileName
   * @param uploadId
   * @return
   */
  @Operation(summary = "分片上传完后合并")
  @ParamTenant
  @GetMapping("/complete")
  default Response<FileMeta> completeMultipartUpload(@PathVariable("tenant_id") String tenantId, @RequestParam("file_name") String fileName, @RequestParam("upload_id") String uploadId) {
    FileMeta fileMeta = null;
    var bucketName = "tmp";

    try {
      ListPartsResponse listMultipart = ossService().listPartsAsync(bucketName, null, fileName, 1000, 0, uploadId, null, null);

      var objectWriteResponse = ossService().completeMultipartUploadAsync(bucketName, null, fileName, uploadId, listMultipart.result().partList().toArray(new Part[]{}), null, null);

      var inputStream = ossService().getObject(bucketName, fileName);
      String fileContentType = objectWriteResponse.headers().get("Content-Type");
      String fileExtension = FilenameUtils.getExtension(fileName);
      var media = MultiMediaUtils.extractMediaInfo(inputStream, fileContentType, fileExtension);
      Assert.isTrue(media != null, Status.CODE.FAIL_VALIDATION.withMsg("upload file is invalid"));

      var ref = new FileMeta[1];

      fileMeta = fileMeta(media, ref);

    } catch (Exception e) {
      e.printStackTrace();
    }
    if (fileMeta != null) {
      fileMeta.setBucket(bucketName);
      fileMeta.setPath(fileName);
    }
    return Response.success(fileMeta);
  }
}
