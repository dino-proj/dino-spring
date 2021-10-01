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

package org.dinospring.core.sys.login;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "dinospring.module.login")
public class LoginModuleProperties {

  /**
   * Mock数据配置
   */
  private MockProperties mock = new MockProperties();

  /**
   * 登录Token相关配置
   */
  private TokenProperties token = new TokenProperties();

  @Data
  public static class MockProperties {
    /**
     * 允许登录的手机号, 正式环境请不要配置
     */
    private List<String> mobiles;
    /**
     * 允许登录的短信密码，正式环境请不要配置
     */
    private String captcha;
  }

  @Data
  public static class TokenProperties {

    /**
     * 传递签名的HTTP头名字， 默认为"D-auth-token"
     */
    private String httpHeaderName = "D-auth-token";

    /**
     * 是否允许多人登录
     */
    private boolean allowMutiDeviceLogin = true;

    /**
     * 登录token的有效时长，默认2小时
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration loginTokenExpiresIn = Duration.ofHours(2L);

    /**
     * refresh token的有效时长，默认365天
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration refreshTokenExpiresIn = Duration.ofDays(365L);

    /**
     * 签名token的有效时长，默认5分钟
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration signTokenExpiresIn = Duration.ofMinutes(5L);

  }
}
