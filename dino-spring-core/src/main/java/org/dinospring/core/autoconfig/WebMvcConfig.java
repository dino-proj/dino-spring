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

package org.dinospring.core.autoconfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.context.DinoContext;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.pdf.HtmlToPdfMessageConverter;
import org.dinospring.core.sys.tenant.TenantService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 */

@Slf4j
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer, ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Autowired(required = false)
  HtmlToPdfMessageConverter htmlToPdfMessageConverter;

  private CorsConfiguration buildConfig() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    //sessionid 多次访问一致
    corsConfiguration.setAllowCredentials(true);

    // 允许访问的客户端域名
    List<String> allowedOriginPatterns = new ArrayList<>();
    allowedOriginPatterns.add("*");
    // 允许任何域名使用
    corsConfiguration.setAllowedOriginPatterns(allowedOriginPatterns);
    // 允许任何头
    corsConfiguration.addAllowedHeader("*");
    // 允许任何方法（post、get等）
    corsConfiguration.addAllowedMethod("*");
    return corsConfiguration;
  }

  @Bean
  public CorsFilter corsFilter() {
    log.info("--->> mvc: config cors filter");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    // 对接口配置跨域设置
    source.registerCorsConfiguration("/**", this.buildConfig());
    return new CorsFilter(source);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    WebMvcConfigurer.super.addInterceptors(registry);
    log.info("--->> mvc: add TenantSupportInterceptor");
    registry.addInterceptor(this.tenantSupportInterceptor());
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    WebMvcConfigurer.super.configureMessageConverters(converters);

    if (Objects.nonNull(this.htmlToPdfMessageConverter)) {
      log.info("--->> mvc: add HtmlToPdfMessageConverter");
      converters.add(this.htmlToPdfMessageConverter);
    }
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    WebMvcConfigurer.super.extendMessageConverters(converters);
    log.info("--->> mvc: add Image converter");
    // 添加图片转换器
    converters.add(new BufferedImageHttpMessageConverter());

    // 添加自定义的jackson转换器
    for (var i = 0; i < converters.size(); i++) {
      if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
        var converter = (MappingJackson2HttpMessageConverter) converters.get(i);
        var objectMapper = this.applicationContext.getBean("objectMapper", ObjectMapper.class);

        log.info("--->> mvc: config objectMapper to {}", converter);
        converter.setObjectMapper(objectMapper);
        break;
      }
    }
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    log.info("--->> mvc: add TenantArgumentResolver");
    resolvers.add(new TenantArgumentResolver());
    log.info("--->> mvc: add UserArgumentResolver");
    resolvers.add(new UserArgumentResolver());
  }

  @Bean
  public TenantSupportInterceptor tenantSupportInterceptor() {
    return new TenantSupportInterceptor();
  }

  public static class TenantSupportInterceptor implements HandlerInterceptor {

    private static final String TENANT_VAR_NAME = "tenant_id";
    @Autowired
    private DinoContext dinoContext;

    @Autowired
    private TenantService tenantService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
      this.dinoContext.currentTenant(null);
      HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
      HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
      Map<String, String> uriTemplateVars = TypeUtils.cast(request.getAttribute(
          HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
      if (Objects.nonNull(uriTemplateVars) && uriTemplateVars.containsKey(TENANT_VAR_NAME)) {
        var tenant = this.tenantService.getById(uriTemplateVars.get(TENANT_VAR_NAME));

        this.dinoContext.currentTenant(this.tenantService.projection(Tenant.class, tenant));
      }
      return HandlerInterceptor.super.preHandle(request, response, handler);
    }

  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    log.info("--->> setup ContextHelper with applicationContext[id={}]", applicationContext.getId());
    ContextHelper.setApplicationContext(applicationContext);
  }

}
