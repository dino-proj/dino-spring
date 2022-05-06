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

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.dinospring.auth.DinoAuth;
import org.dinospring.auth.aop.AuthzAnnotationPointcutAdvisor;
import org.dinospring.auth.session.AuthInfoProvider;
import org.dinospring.auth.session.AuthSession;
import org.dinospring.auth.session.AuthSessionResolver;
import org.dinospring.auth.session.DefaultAuthSessionOpenFilter;
import org.dinospring.core.security.config.SecurityProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tuuboo
 * @date 2022-04-10 18:52:46
 */

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DinoAuthAutoConfig {

  @Bean
  public Supplier<AuthSession> sessionSupplier() {
    return DinoAuth::getAuthSession;
  }

  @Bean
  public AuthzAnnotationPointcutAdvisor authzMethodPointcutAdvisor(SecurityProperties securityProperties,
      Supplier<AuthSession> sessionSupplier) {

    return new AuthzAnnotationPointcutAdvisor(sessionSupplier);
  }

  @ConditionalOnMissingBean(AuthInfoProvider.class)
  @Bean
  public DinoAuthInfoProvider authInfoProvider() {
    return new DinoAuthInfoProvider();
  }

  @ConditionalOnMissingBean(AuthSessionResolver.class)
  @Bean
  public DinoAuthSessionResolver authSessionHttpResolver(SecurityProperties securityProperties) {
    return new DinoAuthSessionResolver(securityProperties.getHttpHeaderName());
  }

  @ConditionalOnMissingBean(DefaultAuthSessionOpenFilter.class)
  @Bean
  public DefaultAuthSessionOpenFilter authSessionOpenFilter(SecurityProperties securityProperties,
      @Autowired ObjectProvider<AuthSessionResolver<?>> sessionResolverProvider) {
    var filter = new DefaultAuthSessionOpenFilter(sessionResolverProvider.orderedStream().collect(Collectors.toList()));
    // 添加白名单，在白名单里的请求不会打开session
    var whiltes = new ArrayList<>(securityProperties.getWhiteList());
    whiltes.add("/actuator/**");
    whiltes.add("/swagger-ui/**");
    whiltes.add("/swagger-ui.html");
    whiltes.add("/v3/api-doc/**");
    whiltes.add("/v3/api-docs/**");
    whiltes.add("*.html");
    whiltes.add("*.htm");
    whiltes.add("*.css");
    whiltes.add("*.js");
    filter.setWhitelist(whiltes);
    return filter;
  }

}
