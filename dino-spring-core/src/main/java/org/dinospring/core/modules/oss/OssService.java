// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.oss;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Cody LU
 * @author JL
 */

public interface OssService {

  /**
   * 查询Bucket是否存在
   * @param bucketName
   * @return
   * @throws IOException
   */
  boolean hasBucket(@Nonnull String bucketName) throws IOException;

  /**
   * 创建bucket，如果存在则什么都不做
   * @param bucketName
   * @throws IOException
   */
  void createBucket(@Nonnull String bucketName) throws IOException;

  /**
   * 删除Bucket，Bucket中的对象全部删除
   * @param bucketName
   * @throws IOException
   */
  void deleteBucket(@Nonnull String bucketName) throws IOException;

  /**
   * 列出有哪些Bucket
   * @return
   * @throws IOException
   */
  List<BucketMeta> listBuckets() throws IOException;

  /**
   * 列出Bucket中的全部对象
   * @param bucketName
   * @return
   * @throws IOException
   */
  Iterable<ObjectMeta> listObjects(@Nonnull String bucketName) throws IOException;

  /**
   * 列出指定文件夹下的全部对象
   * @param bucketName
   * @param dir
   * @return
   * @throws IOException
   */
  Iterable<ObjectMeta> listObjects(@Nonnull String bucketName, @Nonnull String dir) throws IOException;

  /**
   * 查询指定对象的Meta信息
   * @param bucket
   * @param objectName
   * @return
   * @throws IOException
   */
  ObjectMeta statObject(@Nonnull String bucket, @Nonnull String objectName) throws IOException;

  /**
   * 上传对象
   * @param stream
   * @param bucket
   * @param objectName
   * @throws IOException
   */
  void putObject(InputStream stream, String bucket, String objectName) throws IOException;

  /**
   * 上传对象
   * @param stream
   * @param bucket
   * @param objectName
   * @param contentType
   * @throws IOException
   */
  void putObject(InputStream stream, String bucket, String objectName, String contentType) throws IOException;

  /**
   * 获取对象
   * @param bucket
   * @param objectName
   * @return
   * @throws IOException
   */
  InputStream getObject(@Nonnull String bucket, @Nonnull String objectName) throws IOException;

  /**
   * 将对象写到指定output流
   * @param bucket
   * @param objectName
   * @param out
   * @return
   * @throws IOException
   */
  int trasferObject(@Nonnull String bucket, @Nonnull String objectName, OutputStream out) throws IOException;

  /**
   * 删除对象
   * @param bucket
   * @param objectName
   * @throws IOException
   */
  void deleteObject(@Nonnull String bucket, @Nonnull String objectName) throws IOException;

  /**
   * 将对象移动到指定位置，并删除原对象
   * @param srcBucket
   * @param srcObjectName
   * @param destBucket
   * @param destObjectName
   * @throws IOException
   */
  void moveObject(@Nonnull String srcBucket, @Nonnull String srcObjectName, @Nonnull String destBucket,
      @Nonnull String destObjectName) throws IOException;

  /**
   * 将对象Copy到指定位置，并保留原对象
   * @param srcBucket
   * @param srcObjectName
   * @param destBucket
   * @param destObjectName
   * @throws IOException
   */
  void copyObject(@Nonnull String srcBucket, @Nonnull String srcObjectName, @Nonnull String destBucket,
      @Nonnull String destObjectName) throws IOException;

  /**
   * 获取链接
   * @param bucket
   * @param objectName
   * @return
   */
  String getPresignedObjectUrl(String bucket, String objectName);

  /**
   * 获取链接
   * @param bucket
   * @param objectName
   * @param timeout
   * @param unit
   * @return
   */
  String getPresignedObjectUrl(String bucket, String objectName, Integer timeout, TimeUnit unit);
}
