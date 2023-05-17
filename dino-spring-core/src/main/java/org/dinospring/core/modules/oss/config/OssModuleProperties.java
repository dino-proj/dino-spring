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

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 *
 * @author tuuboo
 */

@Data
@ConfigurationProperties(prefix = OssModuleProperties.PREFIX)
public class OssModuleProperties {
  public static final String PREFIX = "dinospring.module.oss";

  /**
   * 配置Minio
   */
  private MinioProperties minio;

  /**
   * 配置本地oss存储
   */
  private LocalOssProperties local;

  /**
   * 配置腾讯云Cos存储
   */
  private TencentCosProperties tencentCos;

  /**
   * oss服务的URLBase
   */
  private String ossUrlBase;

}
