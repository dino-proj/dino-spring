// Copyright 2021 dinospring.cn
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

package org.dinospring.core.security.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 *
 * @author tuuboo
 */

@Data
@Configuration
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {
  public static final String PREFIX = "dinospring.security";

  /**
   * 是否启用安全机制
   */
  private boolean enabled = true;

  /**
   * 白名单，不需要检查权限的URL列表
   */
  private List<String> whiteList;

  /**
   * 传递签名的HTTP头名字， 默认为"D-auth-token"
   */
  private String httpHeaderName = "D-auth-token";
}
