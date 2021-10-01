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

import lombok.Data;

@Data
public class MinioProperties {
  /**
   * 对象存储服务的URI
   */
  @Nonnull
  private String uri;

  /**
   * Access key账户
   */
  @Nonnull
  private String accessKey;

  /**
   * Secret key秘钥
   */
  @Nonnull
  private String secretKey;
}
