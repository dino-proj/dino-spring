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
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.context.DinoContext;
import org.dinospring.commons.context.DinoContextThreadLocalImpl;
import org.dinospring.core.modules.framework.PageConfig;
import org.dinospring.core.modules.framework.annotion.PageTemplate;
import org.dinospring.data.annotion.AnnotionedJsonTypeIdResolver;
import org.dinospring.data.converts.JacksonCustomerModule;
import org.postgresql.util.PGobject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DinoCoreAutoConfiguration implements ApplicationContextAware {

  @Autowired
  Environment environment;

  @PostConstruct
  public void init() throws IOException {
    //添加Json的继承多态支持器
    AnnotionedJsonTypeIdResolver.addAnnotion(JsonTypeName.class, JsonTypeName::value, "org.dinospring");
    AnnotionedJsonTypeIdResolver.addAnnotion(PageTemplate.class, PageTemplate::name, "org.dinospring");
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ContextHelper.setApplicationContext(applicationContext);

  }

  @Bean({ "jacksonObjectMapper", "objectMapper" })
  @Primary
  @ConditionalOnMissingBean(ObjectMapper.class)
  public ObjectMapper objectMapper() {
    log.info("--->>>> new jacksonObjectMapper");
    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

    objectMapper.registerModule(new JacksonCustomerModule());
    return objectMapper;
  }

  @Bean
  @ConditionalOnMissingBean(Gson.class)
  public Gson gson() {
    return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
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

  @Bean
  @ConditionalOnMissingBean
  public ProjectionFactory projectionFactory() {
    return new SpelAwareProxyProjectionFactory();
  }

  @WritingConverter
  enum PageConfigWritingConverter implements Converter<PageConfig, PGobject> {
    INSTANCE;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PGobject convert(PageConfig source) {
      PGobject pg = new PGobject();
      pg.setType("jsonb");
      try {
        pg.setValue(objectMapper.writeValueAsString(source));
        return pg;
      } catch (JsonProcessingException | SQLException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }

  @ReadingConverter
  enum EntityReadingConverter implements Converter<PGobject, PageConfig> {
    INSTANCE;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PageConfig convert(PGobject pg) {
      try {
        return objectMapper.readValue(pg.getValue(), PageConfig.class);
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }

  @Bean
  @ConditionalOnMissingBean
  public DinoContext dinoContext() {
    return new DinoContextThreadLocalImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public ContextHelper contextHelper(DinoContext dinoContext) {
    ContextHelper.setDinoContext(dinoContext);

    return ContextHelper.INST;
  }

}
