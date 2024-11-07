// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.security;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import cn.dinodev.spring.auth.DinoAuth;
import cn.dinodev.spring.auth.aop.AuthzAnnotationPointcutAdvisor;
import cn.dinodev.spring.auth.session.AuthInfoProvider;
import cn.dinodev.spring.auth.session.AuthSession;
import cn.dinodev.spring.auth.session.AuthSessionResolver;
import cn.dinodev.spring.auth.session.DefaultAuthSessionOpenFilter;
import cn.dinodev.spring.core.security.config.SecurityProperties;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 * @date 2022-04-10 18:52:46
 */

@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DinoAuthAutoConfig {

  @Bean
  OpenApiCustomizer openApiAuthCustomiser(SecurityProperties securityProperties) {
    log.info("--->> api-doc: add securitySchema['dino-auth'] with auth header:{}",
        securityProperties.getAuthHeaderName());
    return openApi -> {
      var components = openApi.getComponents();
      if (Objects.isNull(components)) {
        components = new Components();
        openApi.components(components);
      }
      components.addSecuritySchemes("dino-auth", new SecurityScheme()
          .in(SecurityScheme.In.HEADER)
          .name(securityProperties.getAuthHeaderName())
          .description("请将用户登录后的token放在该头部")
          .type(SecurityScheme.Type.APIKEY));
    };
  }

  @Bean
  OperationCustomizer operationCustomizerAddSecurity() {
    log.info("--->> api-doc: add 'dino-auth' security to operations");
    var sec = new SecurityRequirement().addList("dino-auth");
    return (operation, method) -> {
      operation.addSecurityItem(sec);
      return operation;
    };
  }

  @Bean
  Supplier<AuthSession> sessionSupplier() {
    return DinoAuth::getAuthSession;
  }

  @Bean
  AuthzAnnotationPointcutAdvisor authzMethodPointcutAdvisor(SecurityProperties securityProperties,
      Supplier<AuthSession> sessionSupplier, BeanFactory beanFactory) {

    return new AuthzAnnotationPointcutAdvisor(sessionSupplier, beanFactory);
  }

  @ConditionalOnMissingBean(AuthInfoProvider.class)
  @Bean
  DinoAuthInfoProvider authInfoProvider() {
    return new DinoAuthInfoProvider();
  }

  @ConditionalOnMissingBean(AuthSessionResolver.class)
  @Bean
  DinoAuthSessionResolver authSessionHttpResolver(SecurityProperties securityProperties) {
    return new DinoAuthSessionResolver(securityProperties.getAuthHeaderName());
  }

  @ConditionalOnMissingBean(DefaultAuthSessionOpenFilter.class)
  @Bean
  DefaultAuthSessionOpenFilter authSessionOpenFilter(SecurityProperties securityProperties,
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
