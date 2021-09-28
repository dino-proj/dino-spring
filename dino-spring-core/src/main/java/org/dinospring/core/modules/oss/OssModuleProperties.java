package org.dinospring.core.modules.oss;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = OssModuleProperties.PREFIX)
public class OssModuleProperties {
  public static final String PREFIX = "dinospring.module.oss";

  /**
   * 配置Minio
   */
  private MinioProperties minio;

  @Bean
  @ConditionalOnClass(MinioClient.class)
  public MinioClient minioClient(OssModuleProperties fsProps) {
    return MinioClient.builder().endpoint(fsProps.minio.getUri())
        .credentials(fsProps.minio.getAccessKey(), fsProps.minio.getSecretKey()).build();
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

  /**
   * 配置本地oss存储
   */
  private LocalProperties local;

  @Data
  public static class LocalProperties {

    /**
     * 本地存储文件夹路径
     */
    private String baseDir;
  }
}
