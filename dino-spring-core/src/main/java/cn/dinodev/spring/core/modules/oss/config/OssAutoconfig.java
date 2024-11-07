// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.oss.config;

import java.io.IOException;

import com.qcloud.cos.COSClient;

import cn.dinodev.spring.core.modules.oss.impl.LocalOssService;
import cn.dinodev.spring.core.modules.oss.impl.MinioOssService;
import cn.dinodev.spring.core.modules.oss.impl.TencentOssService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

/**
 *
 * @author Cody Lu
 */

@Configuration
@EnableConfigurationProperties(OssModuleProperties.class)
public class OssAutoconfig {

  @Bean
  @ConditionalOnClass(MinioClient.class)
  public MinioOssService minioOssService(OssModuleProperties ossProps) {
    return new MinioOssService(ossProps.getMinio());
  }

  @Bean
  @ConditionalOnProperty(prefix = OssModuleProperties.PREFIX, name = "local")
  public LocalOssService localOssService(OssModuleProperties ossProps) throws IOException {
    return new LocalOssService(ossProps.getLocal());
  }

  @Bean
  @ConditionalOnClass(COSClient.class)
  @ConditionalOnProperty(prefix = OssModuleProperties.PREFIX, name = "tencent-cos")
  public TencentOssService tencentOssService(OssModuleProperties ossProps) {
    return new TencentOssService(ossProps.getTencentCos());
  }

}
