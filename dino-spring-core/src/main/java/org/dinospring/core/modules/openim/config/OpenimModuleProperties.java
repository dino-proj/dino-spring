// Copyright 2022 dinodev.cn
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

package org.dinospring.core.modules.openim.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * openim的配置
 * @author tuuboo
 * @date 2022-04-13 03:11:08
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = OpenimModuleProperties.PREFIX, name = "uri")
@ConfigurationProperties(prefix = OpenimModuleProperties.PREFIX)
public class OpenimModuleProperties {
  public static final String PREFIX = "dinospring.module.openim";

  /**
   * openim服务器地址，格式为：http://host:port
   */
  private String uri;

  /**
   * openim管理员userID，此处的userID必须为配置文件config/config.yaml的appManagerUid的其中一个
   */
  private String adminId;

  /**
   * openim秘钥
   */
  private String secret;

}