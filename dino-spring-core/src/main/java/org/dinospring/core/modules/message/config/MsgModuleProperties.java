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

package org.dinospring.core.modules.message.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author tuuboo
 */

@Data
@Configuration
@ConfigurationProperties(prefix = MsgModuleProperties.PREFIX)
public class MsgModuleProperties {
  public static final String PREFIX = "dinospring.module.msg";

  /**
   * 短信配置
   */
  private SmsProperties sms;

  @Data
  public static class SmsProperties {
    /**
     * 短信厂商
     */
    private SmsVendor vendor;
  }

  public enum SmsVendor {
    /**
     * 阿里巴巴短信通道
     */
    ALI,
    /**
     * 腾讯短信通道
     */
    TENCENT;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }
}
