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

package org.dinospring.data.autoconfig;

import java.sql.DatabaseMetaData;
import java.util.Locale;

import javax.annotation.PostConstruct;

import com.botbrain.dino.sql.dialect.Dialect;
import com.botbrain.dino.sql.dialect.MysqlDialect;
import com.botbrain.dino.sql.dialect.PostgreSQLDialect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.dinospring.commons.autoconfig.DinoCommonsAutoConfiguration;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.data.converts.JacksonCustomerModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;

import lombok.var;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 */

@Slf4j
@Configuration
@ImportAutoConfiguration(DinoCommonsAutoConfiguration.class)
@AutoConfigureAfter(DinoCommonsAutoConfiguration.class)
public class DinoDataAutoConfiguration {

  @Autowired
  private ContextHelper contextHelper;

  @PostConstruct
  public void check() {
    Assert.notNull(contextHelper, "contextHelper should init before DinoDataAutoConfiguration");
  }

  @Bean
  @ConditionalOnMissingBean
  public Dialect dialect(JdbcOperations jdbcOperations) {

    return jdbcOperations.execute((ConnectionCallback<Dialect>) conn -> {
      DatabaseMetaData metaData = conn.getMetaData();

      String name = metaData.getDatabaseProductName().toLowerCase(Locale.ENGLISH);

      if (name.contains("mysql") || name.contains("mariadb")) {
        return new MysqlDialect();
      }
      if (name.contains("postgresql")) {
        return new PostgreSQLDialect();
      }

      log.warn("Couldn't determine DB Dialect for {}", name);
      return new Dialect.DEFAULT();
    });
  }

  @Bean({ "jacksonObjectMapper", "objectMapper" })
  @Primary
  @ConditionalOnMissingBean
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
  @ConditionalOnMissingBean
  public Gson gson() {
    return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  }

  @Bean
  @ConditionalOnMissingBean
  public ProjectionFactory projectionFactory() {
    return new SpelAwareProxyProjectionFactory();
  }

  @Bean("dataConversionService")
  public ConversionService dataConversionService(ApplicationContext applicationContext) {
    log.info("--->>>> new dataConversionService");
    var dataConversionService = new DefaultConversionService();
    ApplicationConversionService.addApplicationConverters(dataConversionService);

    var genericConverters = applicationContext.getBeansOfType(GenericConverter.class);
    for (var kv : genericConverters.entrySet()) {
      log.info("register GenericConverter[{} @{}] to dataConversionService", kv.getKey(), kv.getValue());
      dataConversionService.addConverter(kv.getValue());
    }
    return dataConversionService;
  }
}
