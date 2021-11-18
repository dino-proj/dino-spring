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

import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
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
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

/**
 *
 * @author tuuboo
 * @author JL
 */

public class MinioOssService implements OssService {

  private MinioClient minioClient;

  public MinioOssService(@Nonnull MinioProperties properties) {
    minioClient = MinioClient.builder().endpoint(properties.getUri())
      .credentials(properties.getAccessKey(), properties.getSecretKey()).build();
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
    String url = null;
    try {
      url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucket).object(objectName).method(Method.GET).build());
    } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
      InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException | ServerException e) {
      e.printStackTrace();
    }
    return url;
  }

}
