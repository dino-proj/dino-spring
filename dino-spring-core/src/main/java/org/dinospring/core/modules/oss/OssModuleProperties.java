package org.dinospring.core.modules.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "dinospring.module.oss")
public class OssModuleProperties {
  private MinioProperties minio;

  @Bean
  public MinioClient minioClient(OssModuleProperties fsProps) {
    MinioClient minioClient = MinioClient.builder().endpoint(fsProps.minio.getUri())
        .credentials(fsProps.minio.getAccessKey(), fsProps.minio.getSecretKey()).build();
    return minioClient;
  }

  @Data
  public static class MinioProperties {
    /**
     * 对象存储服务的URI
     */
    private String uri;

    /**
     * Access key账户
     */
    private String accessKey;

    /**
     * Secret key秘钥
     */
    private String secretKey;
  }
}
