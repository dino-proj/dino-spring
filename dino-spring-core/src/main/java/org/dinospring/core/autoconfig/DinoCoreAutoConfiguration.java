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

package org.dinospring.core.autoconfig;

import java.io.IOException;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonTypeName;

import org.dinospring.core.modules.framework.annotion.PageTemplate;
import org.dinospring.data.annotion.AnnotionedJsonTypeIdResolver;
import org.dinospring.data.autoconfig.DinoDataAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 */

@Configuration
@Slf4j
@AutoConfigureAfter(DinoDataAutoConfiguration.class)
public class DinoCoreAutoConfiguration {

  @Autowired
  Environment environment;

  @PostConstruct
  public void init() throws IOException {
    //添加Json的继承多态支持器
    AnnotionedJsonTypeIdResolver.addAnnotion(JsonTypeName.class, JsonTypeName::value, "org.dinospring");
    AnnotionedJsonTypeIdResolver.addAnnotion(PageTemplate.class, PageTemplate::name, "org.dinospring");
  }

  @Bean
  @ConditionalOnMissingBean(RestTemplate.class)
  public RestTemplate restTemplate() {
    log.info("--->>>> new restTemplate");
    SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
    simpleClientHttpRequestFactory.setConnectTimeout(5000);
    simpleClientHttpRequestFactory.setReadTimeout(5000);
    return new RestTemplate(simpleClientHttpRequestFactory);
  }

}
