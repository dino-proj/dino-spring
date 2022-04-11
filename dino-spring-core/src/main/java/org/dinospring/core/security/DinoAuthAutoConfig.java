// Copyright 2022 dinospring.cn
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

package org.dinospring.core.security;

import java.util.function.Supplier;

import org.dinospring.auth.DinoAuth;
import org.dinospring.auth.aop.AuthzAnnotationPointcutAdvisor;
import org.dinospring.auth.session.AuthSession;
import org.dinospring.auth.session.AuthSessionHttpResolver;
import org.dinospring.auth.session.DefaultAuthSessionOpenFilter;
import org.dinospring.core.security.config.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tuuboo
 * @date 2022-04-10 18:52:46
 */

@Configuration
public class DinoAuthAutoConfig {

  @ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
  @Bean
  public AuthzAnnotationPointcutAdvisor authzMethodPointcutAdvisor(SecurityProperties securityProperties,
      Supplier<AuthSession> sessionSupplier) {

    return new AuthzAnnotationPointcutAdvisor(sessionSupplier);
  }

  @Bean
  public Supplier<AuthSession> sessionSupplier() {
    return DinoAuth::getAuthSession;
  }

  @Bean
  public DinoAuthSessionResolver authSessionHttpResolver(SecurityProperties securityProperties) {
    return new DinoAuthSessionResolver(securityProperties.getHttpHeaderName());
  }

  @Bean
  public DefaultAuthSessionOpenFilter authSessionOpenFilter(AuthSessionHttpResolver<?> sessionResolver) {
    return new DefaultAuthSessionOpenFilter(sessionResolver);
  }

}
