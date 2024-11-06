// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.oss.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.iterators.LazyIteratorChain;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.core.modules.oss.BucketMeta;
import org.dinospring.core.modules.oss.ObjectMeta;
import org.dinospring.core.modules.oss.OssService;
import org.dinospring.core.modules.oss.config.TencentCosProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 *
 * @author Cody Lu
 * @author JL
 */

public class TencentOssService implements OssService {

  private COSClient cosClient;

  public TencentOssService(TencentCosProperties properties) {

    // 1 初始化用户身份信息（secretId, secretKey）。
    COSCredentials cred = new BasicCOSCredentials(properties.getSecretId(), properties.getSecretKey());
    // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
    // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
    Region region = new Region(properties.getRegion());
    ClientConfig clientConfig = new ClientConfig(region);
    // 配置代理
    if (StringUtils.isNotBlank(properties.getHttpProxy())) {
      var ipPort = StringUtils.split(properties.getHttpProxy(), ":");
      clientConfig.setHttpProxyIp(ipPort[0]);
      clientConfig.setHttpProxyPort(Integer.parseInt(ipPort[1]));
    }
    // 3 生成 cos 客户端。
    cosClient = new COSClient(cred, clientConfig);
  }

  @Override
  public boolean hasBucket(String bucketName) throws IOException {
    try {
      return cosClient.doesBucketExist(bucketName);
    } catch (CosServiceException e) {
      throw new IOException("Error occured while check bucket exist[" + bucketName + "]", e);
    }
  }

  @Override
  public void createBucket(String bucketName) throws IOException {
    CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
    // 设置 bucket 的权限为 Private(私有读写)、其他可选有 PublicRead（公有读私有写）、PublicReadWrite（公有读写）
    createBucketRequest.setCannedAcl(CannedAccessControlList.Private);
    try {
      cosClient.createBucket(createBucketRequest);
    } catch (CosClientException clientException) {
      throw new IOException("Error occured while create bucket[" + bucketName + "]", clientException);
    }

  }

  @Override
  public void deleteBucket(String bucketName) throws IOException {
    try {
      cosClient.deleteBucket(bucketName);
    } catch (CosClientException clientException) {
      throw new IOException("Error occured while delete bucket[" + bucketName + "]", clientException);
    }
  }

  @Override
  public List<BucketMeta> listBuckets() throws IOException {
    try {
      List<Bucket> buckets = cosClient.listBuckets();
      return buckets.stream().map(b -> BucketMeta.of(b.getName(), b.getCreationDate())).collect(Collectors.toList());
    } catch (

    CosClientException clientException) {
      throw new IOException("Error occured while list buckets", clientException);
    }
  }

  @Override
  public Iterable<ObjectMeta> listObjects(String bucketName) throws IOException {
    return listObjects(bucketName, null);
  }

  @Override
  public Iterable<ObjectMeta> listObjects(String bucketName, String dir) throws IOException {

    // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
    ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
    // 设置bucket名称
    listObjectsRequest.setBucketName(bucketName);
    // prefix表示列出的object的key以prefix开始
    if (StringUtils.isNotBlank(dir)) {
      listObjectsRequest.setPrefix(StringUtils.appendIfMissing(dir, "/", "/"));
    }
    // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
    listObjectsRequest.setDelimiter("/");
    // 设置最大遍历出多少个对象, 一次listobject最大支持1000
    listObjectsRequest.setMaxKeys(1000);
    AtomicBoolean hasMore = new AtomicBoolean(true);
    return IteratorUtils.asIterable(new LazyIteratorChain<ObjectMeta>() {

      @Override
      protected Iterator<? extends ObjectMeta> nextIterator(int count) {
        if (!hasMore.get()) {
          return null;
        }
        try {
          var objectListing = cosClient.listObjects(listObjectsRequest);
          var dirs = objectListing.getCommonPrefixes().stream().map(p -> ObjectMeta.ofDir(p, null))
              .collect(Collectors.toList());
          var files = objectListing.getObjectSummaries().stream()
              .map(p -> ObjectMeta.ofFile(p.getKey(), p.getSize(), p.getLastModified())).collect(Collectors.toList());

          // for next
          listObjectsRequest.setMarker(objectListing.getNextMarker());
          hasMore.set(objectListing.isTruncated());

          return IteratorUtils.chainedIterator(dirs.iterator(), files.iterator());
        } catch (CosClientException e) {
          throw new IllegalStateException("Error occured while list files", e);
        }
      }
    });
  }

  @Override
  public ObjectMeta statObject(String bucket, String objectName) throws IOException {
    try {
      var obj = cosClient.getObjectMetadata(bucket, objectName);
      return ObjectMeta.ofFile(objectName, obj.getContentLength(), obj.getLastModified());
    } catch (CosServiceException cse) {
      if (cse.getStatusCode() == 404) {
        throw new FileNotFoundException(bucket + ": " + objectName);
      }
      throw new IOException("Error occured while stat file:" + objectName, cse);
    }

  }

  @Override
  public void putObject(InputStream stream, String bucket, String objectName) throws IOException {
    try {
      var meta = new ObjectMetadata();
      var putObjectRequest = new PutObjectRequest(bucket, objectName, stream, meta);
      cosClient.putObject(putObjectRequest);
    } catch (CosServiceException cse) {
      throw new IOException("Error occured while put file:" + objectName, cse);
    }
  }

  @Override
  public void putObject(InputStream stream, String bucket, String objectName, String contentType) throws IOException {
    try {
      var meta = new ObjectMetadata();
      meta.setHeader("Content-Type", contentType);
      var putObjectRequest = new PutObjectRequest(bucket, objectName, stream, meta);
      cosClient.putObject(putObjectRequest);
    } catch (CosServiceException cse) {
      throw new IOException("Error occured while put file:" + objectName, cse);
    }
  }

  @Override
  public InputStream getObject(String bucket, String objectName) throws IOException {
    try {
      var getObjectRequest = new GetObjectRequest(bucket, objectName);
      COSObject cosObject = cosClient.getObject(getObjectRequest);
      return cosObject.getObjectContent();
    } catch (CosServiceException cse) {
      if (cse.getStatusCode() == 404) {
        throw new FileNotFoundException(bucket + ": " + objectName);
      }
      throw new IOException("Error occured while get file:" + objectName, cse);
    }
  }

  @Override
  public int trasferObject(String bucket, String objectName, OutputStream out) throws IOException {
    try {
      var getObjectRequest = new GetObjectRequest(bucket, objectName);
      COSObject cosObject = cosClient.getObject(getObjectRequest);
      return IOUtils.copy(cosObject.getObjectContent(), out);
    } catch (CosServiceException cse) {
      if (cse.getStatusCode() == 404) {
        throw new FileNotFoundException(bucket + ": " + objectName);
      }
      throw new IOException("Error occured while trasfer file:" + objectName, cse);
    }
  }

  @Override
  public void deleteObject(String bucket, String objectName) throws IOException {
    try {
      cosClient.deleteObject(bucket, objectName);
    } catch (CosServiceException cse) {
      if (cse.getStatusCode() == 404) {
        throw new FileNotFoundException(bucket + ": " + objectName);
      }
      throw new IOException("Error occured while delete file:" + objectName, cse);
    }
  }

  @Override
  public void moveObject(String srcBucket, String srcObjectName, String destBucket, String destObjectName)
      throws IOException {
    copyObject(srcBucket, srcObjectName, destBucket, destObjectName);
    deleteObject(srcBucket, srcObjectName);
  }

  @Override
  public void copyObject(String srcBucket, String srcObjectName, String destBucket, String destObjectName)
      throws IOException {
    try {
      cosClient.copyObject(srcBucket, srcObjectName, destBucket, destObjectName);
    } catch (CosServiceException cse) {
      if (cse.getStatusCode() == 404) {
        throw new FileNotFoundException(srcBucket + ": " + srcObjectName);
      }
      throw new IOException("Error occured while copy file:" + srcObjectName, cse);
    }

  }

  @Override
  public String getPresignedObjectUrl(String bucket, String objectName) {
    return getPresignedObjectUrl(bucket, objectName, null, null);
  }

  @Override
  public String getPresignedObjectUrl(String bucket, String objectName, Integer timeout, TimeUnit unit) {
    GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket, objectName, HttpMethodName.GET);
    if (Objects.nonNull(timeout) && Objects.nonNull(unit)) {
      Date expiration = new Date(System.currentTimeMillis() + unit.toMillis(timeout));
      urlRequest.setExpiration(expiration);
    }

    return cosClient.generatePresignedUrl(urlRequest).toString();
  }
}
