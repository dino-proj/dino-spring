package org.dinospring.core.modules.oss;

import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListPartsResponse;
import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Part;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Jack Liu
 * @Date: 2022/10/24 14:32
 */
public class AsyncMinIoClient extends MinioAsyncClient {

  public AsyncMinIoClient(MinioAsyncClient client) {
    super(client);
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
  public CompletableFuture<CreateMultipartUploadResponse> createMultipartUploadAsync(String bucketName, String region, String objectName, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) throws InsufficientDataException, InternalException, IOException, NoSuchAlgorithmException, XmlParserException, InvalidKeyException {
    return super.createMultipartUploadAsync(bucketName, region, objectName, headers, extraQueryParams);
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
  public CompletableFuture<ObjectWriteResponse> completeMultipartUploadAsync(String bucketName, String region, String objectName, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws InsufficientDataException, InternalException, IOException, NoSuchAlgorithmException, XmlParserException, InvalidKeyException {
    return super.completeMultipartUploadAsync(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams);
  }

  /**
   * 查询分片数据
   *  @param bucketName      存储桶
   * @param region           区域
   * @param objectName       对象名
   * @param uploadId         上传ID
   * @param extraHeaders     额外消息头
   * @param extraQueryParams 额外查询参数
   * @return
   */
  @Override
  public CompletableFuture<ListPartsResponse> listPartsAsync(String bucketName, String region, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws InsufficientDataException, InternalException, IOException, NoSuchAlgorithmException, XmlParserException, InvalidKeyException {
    return super.listPartsAsync(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams);
  }


  /**
   * 创建文件预上传地址
   * @param bucketName 存储桶
   * @param objectName 对象名
   * @param partNumber 分片数量
   * @param uploadId 上传ID
   * @return
   */
  public String createUploadUrlAsync(String bucketName,String objectName,Integer partNumber, String uploadId) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
    return this.getPresignedObjectUrl(
      GetPresignedObjectUrlArgs.builder()
        .method(Method.PUT)
        .bucket(bucketName)
        .object(objectName)
        .expiry(60 * 60 * 24)
        .extraQueryParams(
          newMultimap("partNumber", Integer.toString(partNumber), "uploadId", uploadId)
        )
        .build()
    );
  }
}
