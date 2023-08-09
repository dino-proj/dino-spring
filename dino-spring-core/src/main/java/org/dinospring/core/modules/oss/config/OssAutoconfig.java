// Copyright 2021 dinodev.cn
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

package org.dinospring.core.modules.oss.config;

import java.io.IOException;

import com.qcloud.cos.COSClient;

import org.dinospring.core.modules.oss.impl.LocalOssService;
import org.dinospring.core.modules.oss.impl.MinioOssService;
import org.dinospring.core.modules.oss.impl.TencentOssService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

/**
 *
 * @author Cody LU
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
