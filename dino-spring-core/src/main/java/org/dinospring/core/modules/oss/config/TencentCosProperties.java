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

package org.dinospring.core.modules.oss.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Data;

@Data
public class TencentCosProperties {
  /**
   * SECRETID和SECRETKEY请登录访问管理控制台进行查看和管理
   */
  @Nonnull
  private String secretId;

  /**
   * SECRETID和SECRETKEY请登录访问管理控制台进行查看和管理
   */
  @Nonnull
  private String secretKey;

  /**
   * bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
   */
  @Nonnull
  private String region;

  /**
   * 代理信息，格式：[ip]:[port]，例如： 192.168.1.1:8080
   */
  @Nullable
  private String httpProxy;
}