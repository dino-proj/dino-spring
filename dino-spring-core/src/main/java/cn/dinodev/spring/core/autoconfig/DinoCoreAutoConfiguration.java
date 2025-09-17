// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.autoconfig;

import java.io.IOException;

import cn.dinodev.spring.commons.json.AnnotionedJsonTypeIdResolver;
import cn.dinodev.spring.core.modules.framework.annotion.PageTemplate;
import cn.dinodev.spring.core.security.DinoAuthAutoConfig;
import cn.dinodev.spring.data.autoconfig.DinoDataAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Slf4j
@Configuration
@ImportAutoConfiguration({ DinoDataAutoConfiguration.class, DinoAuthAutoConfig.class })
@AutoConfigureAfter(DinoDataAutoConfiguration.class)
@AutoConfigureBefore(DinoAuthAutoConfig.class)
public class DinoCoreAutoConfiguration {

  @Autowired
  Environment environment;

  @PostConstruct
  public void init() throws IOException {
    //添加Json的继承多态支持器
    AnnotionedJsonTypeIdResolver.addAnnotion(JsonTypeName.class, JsonTypeName::value, "cn.dinodev.spring");
    AnnotionedJsonTypeIdResolver.addAnnotion(PageTemplate.class, PageTemplate::name, "cn.dinodev.spring");
  }

  @Bean
  @ConditionalOnMissingBean(RestTemplate.class)
  public RestTemplate restTemplate() {
    log.info("--->> create restTemplate");
    SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
    simpleClientHttpRequestFactory.setConnectTimeout(5000);
    simpleClientHttpRequestFactory.setReadTimeout(5000);
    return new RestTemplate(simpleClientHttpRequestFactory);
  }

}
