package org.dinospring.core.modules.oss.config;

import java.io.IOException;

import org.dinospring.core.modules.oss.impl.LocalOssService;
import org.dinospring.core.modules.oss.impl.MinioOssService;
import org.dinospring.core.modules.oss.impl.TencentOssService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
@AutoConfigureAfter(OssModuleProperties.class)
public class OssAutoconfig {

  @Bean
  @ConditionalOnClass(MinioClient.class)
  @ConditionalOnProperty(prefix = OssModuleProperties.PREFIX, name = "minio")
  public MinioOssService minioOssService(OssModuleProperties fsProps) {
    return new MinioOssService(fsProps.getMinio());
  }

  @Bean
  @ConditionalOnProperty(prefix = OssModuleProperties.PREFIX, name = "local")
  public LocalOssService localOssService(OssModuleProperties fsProps) throws IOException {
    return new LocalOssService(fsProps.getLocal());
  }

  @Bean
  @ConditionalOnClass(MinioClient.class)
  @ConditionalOnProperty(prefix = OssModuleProperties.PREFIX, name = "tencent-cos")
  public TencentOssService tencentOssService(OssModuleProperties fsProps) throws IOException {
    return new TencentOssService(fsProps.getTencentCos());
  }

}
