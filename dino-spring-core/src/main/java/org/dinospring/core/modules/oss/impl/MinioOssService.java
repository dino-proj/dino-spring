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

package org.dinospring.core.modules.oss.impl;

import com.google.common.collect.Multimap;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.CreateMultipartUploadResponse;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.ListPartsResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.Part;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.core.modules.oss.AsyncMinIoClient;
import org.dinospring.core.modules.oss.BucketMeta;
import org.dinospring.core.modules.oss.ObjectMeta;
import org.dinospring.core.modules.oss.OssService;
import org.dinospring.core.modules.oss.config.MinioProperties;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author tuuboo
 * @author JL
 */

public class MinioOssService implements OssService {

  private MinioClient minioClient;

  private AsyncMinIoClient asyncMinIoClient;

  public MinioOssService(@Nonnull MinioProperties properties) {
    minioClient = MinioClient.builder().endpoint(properties.getUri())
      .credentials(properties.getAccessKey(), properties.getSecretKey()).build();

    asyncMinIoClient = new AsyncMinIoClient(MinioAsyncClient.builder().endpoint(properties.getUri())
      .credentials(properties.getAccessKey(), properties.getSecretKey()).build());
  }

  @Override
  public boolean hasBucket(String bucketName) throws IOException {
    try {
      return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException e) {
      throw new IOException("Error occured while query bucket[" + bucketName + "] exidts", e);
    }
  }

  @Override
  public void createBucket(String bucketName) throws IOException {
    boolean found = this.hasBucket(bucketName);
    if (found) {
      return;
    }
    try {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException e) {
      throw new IOException("Error occured while create bucket[" + bucketName + "]", e);
    }

  }

  @Override
  public void deleteBucket(String bucketName) throws IOException {
    try {
      minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException e) {
      throw new IOException("Error occured while delete bucket[" + bucketName + "]", e);
    }
  }

  @Override
  public List<BucketMeta> listBuckets() throws IOException {
    try {
      var list = minioClient.listBuckets();
      return list.stream().map(b -> BucketMeta.of(b.name(), Date.from(b.creationDate().toInstant())))
        .collect(Collectors.toList());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
      throw new IOException("Error occured while list buckets", e);
    }
  }

  @Override
  public Iterable<ObjectMeta> listObjects(String bucketName) throws IOException {
    Iterable<Result<Item>> results = minioClient
      .listObjects(ListObjectsArgs.builder().bucket(bucketName).recursive(true).build());
    return IterableUtils.transformedIterable(results, r -> {
      try {
        return ObjectMeta.builder().name(r.get().objectName()).build();
      } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
        | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
        | XmlParserException | IOException e) {
        throw new IllegalStateException("Error occured while list files", e);
      }
    });

  }

  @Override
  public Iterable<ObjectMeta> listObjects(String bucketName, String dir) throws IOException {

    Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName)
      .prefix(StringUtils.appendIfMissing(dir, "/", "/")).recursive(true).build());
    return IterableUtils.transformedIterable(results, r -> {
      try {
        return ObjectMeta.builder().name(r.get().objectName()).build();
      } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
        | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
        | XmlParserException | IOException e) {
        throw new IllegalStateException("Error occured while list files", e);
      }
    });

  }

  @Override
  public ObjectMeta statObject(String bucket, String objectName) throws IOException {
    try {
      var stat = minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(objectName).build());
      return ObjectMeta.builder().name(stat.object()).size(stat.size()).dir(false).build();
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException e) {
      throw new IOException("Error occured while stat file:" + objectName, e);
    }
  }

  @Override
  public void putObject(InputStream stream, String bucket, String objectName) throws IOException {
    try {
      minioClient
        .putObject(PutObjectArgs.builder().bucket(bucket).object(objectName).stream(stream, -1, 10485760).build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException e) {
      throw new IllegalStateException("Error occured while put file:" + objectName, e);
    }
  }

  @Override
  public void putObject(InputStream stream, String bucket, String objectName, String contentType) throws IOException {
    try {
      minioClient
        .putObject(PutObjectArgs.builder().bucket(bucket).contentType(contentType).object(objectName).stream(stream, -1, 10485760).build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException e) {
      throw new IllegalStateException("Error occured while put file:" + objectName, e);
    }
  }

  @Override
  public InputStream getObject(String bucket, String objectName) throws IOException {
    try {
      return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException e) {
      throw new IOException("Error occured while download file:" + objectName, e);
    }
  }

  @Override
  public int trasferObject(String bucket, String objectName, OutputStream out) throws IOException {
    try {
      var in = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).build());
      return IOUtils.copy(in, out);
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException e) {
      throw new IOException("Error occured while trasfer file:" + objectName, e);
    }

  }

  @Override
  public void deleteObject(String bucket, String objectName) throws IOException {
    try {
      minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException e) {
      throw new IOException("Error occured while delete file:" + objectName, e);
    }
  }

  @Override
  public void moveObject(String srcBucket, String srcObjectName, String destBucket, String destObjectName)
    throws IOException {
    try {
      minioClient.copyObject(CopyObjectArgs.builder().bucket(destBucket).object(destObjectName)
        .source(CopySource.builder().bucket(srcBucket).object(srcObjectName).build()).build());

      this.deleteObject(srcBucket, srcObjectName);
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException | IOException e) {
      throw new IOException("Error occured while move file:" + srcObjectName, e);
    }

  }

  @Override
  public void copyObject(String srcBucket, String srcObjectName, String destBucket, String destObjectName)
    throws IOException {
    try {
      minioClient.copyObject(CopyObjectArgs.builder().bucket(destBucket).object(destObjectName)
        .source(CopySource.builder().bucket(srcBucket).object(srcBucket).build()).build());
    } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
      | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
      | IllegalArgumentException | IOException e) {
      throw new IOException("Error occured while copy file:" + srcObjectName, e);
    }

  }


  @Override
  public String getPresignedObjectUrl(String bucket, String objectName) {
    return getPresignedObjectUrl(bucket, objectName, null, null);
  }

  @Override
  public String getPresignedObjectUrl(String bucket, String objectName, Integer timeout, TimeUnit unit) {
    String url = null;
    try {
      GetPresignedObjectUrlArgs.Builder builder = GetPresignedObjectUrlArgs.builder().bucket(bucket).object(objectName).method(Method.GET);
      if (timeout != null && unit != null) {
        builder.expiry(timeout, unit);
      }
      url = minioClient.getPresignedObjectUrl(builder.build());
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
      InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException | ServerException e) {
      e.printStackTrace();
    }
    return url;
  }

  /**
   * 创建分片上传请求
   *  @param bucketName       存储桶
   * @param region           区域
   * @param objectName       对象名
   * @param headers          消息头
   * @param extraQueryParams 额外查询参数
   * @return
   */
  @Override
  public CreateMultipartUploadResponse createMultipartUploadAsync(String bucketName, String region, String objectName, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) {
    CreateMultipartUploadResponse createMultipartUploadResponse = null;
    try {
      createMultipartUploadResponse = asyncMinIoClient.createMultipartUploadAsync(bucketName, region, objectName, headers, extraQueryParams).get();
    } catch (InterruptedException | ExecutionException | InsufficientDataException | IOException | NoSuchAlgorithmException | XmlParserException | InvalidKeyException | InternalException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    return createMultipartUploadResponse;
  }

  /**
   * 完成分片上传，执行合并文件
   *  @param bucketName       存储桶
   * @param region           区域
   * @param objectName       对象名
   * @param uploadId         上传ID
   * @param parts            分片
   * @param extraHeaders     额外消息头
   * @param extraQueryParams 额外查询参数
   * @return
   */
  @Override
  public ObjectWriteResponse completeMultipartUploadAsync(String bucketName, String region, String objectName, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) {
    ObjectWriteResponse objectWriteResponse = null;
    try {
      objectWriteResponse = asyncMinIoClient.completeMultipartUploadAsync(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams).get();
    } catch (InterruptedException | ExecutionException | InsufficientDataException | InternalException | IOException | NoSuchAlgorithmException | XmlParserException | InvalidKeyException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    return objectWriteResponse;
  }

  /**
   * 查询分片数据
   *  @param bucketName       存储桶
   * @param region           区域
   * @param objectName       对象名
   * @param uploadId         上传ID
   * @param extraHeaders     额外消息头
   * @param extraQueryParams 额外查询参数
   * @return
   */
  public ListPartsResponse listPartsAsync(String bucketName, String region, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) {
    ListPartsResponse listPartsResponse = null;
    try {
      listPartsResponse = asyncMinIoClient.listPartsAsync(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams).get();
    } catch (InsufficientDataException | InternalException | IOException | NoSuchAlgorithmException | XmlParserException | InvalidKeyException | ExecutionException | InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
    return listPartsResponse;
  }

  /**
   * 创建文件预上传地址
   * @param bucketName 存储桶
   * @param objectName 对象名
   * @param partNumber 分片数量
   * @param uploadId 上传ID
   * @return
   */
  @Override
  public String createUploadUrlAsync(String bucketName, String objectName, Integer partNumber, String uploadId) {
    String uploadUrlAsync = null;
    try {
      uploadUrlAsync = asyncMinIoClient.createUploadUrlAsync(bucketName, objectName, partNumber, uploadId);
    } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
      e.printStackTrace();
    }
    return uploadUrlAsync;
  }

}
