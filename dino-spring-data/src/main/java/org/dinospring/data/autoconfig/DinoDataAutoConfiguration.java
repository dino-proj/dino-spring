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

package org.dinospring.data.autoconfig;

import java.sql.DatabaseMetaData;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.dinospring.commons.autoconfig.DinoCommonsAutoConfiguration;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.json.JsonDiscriminatorModule;
import org.dinospring.data.converts.JacksonCustomerModule;
import org.dinospring.data.sql.dialect.Dialect;
import org.dinospring.data.sql.dialect.MysqlDialect;
import org.dinospring.data.sql.dialect.PostgreSQLDialect;
import org.dinospring.data.sql.dialect.SnakeNamingConversition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
      Dialect dialect = Dialect.ofDefault();

      String name = metaData.getDatabaseProductName().toLowerCase(Locale.ENGLISH);

      if (name.contains("mysql") || name.contains("mariadb")) {
        dialect = new MysqlDialect(metaData, new SnakeNamingConversition());
      }
      if (name.contains("postgresql")) {
        dialect = new PostgreSQLDialect(metaData, new SnakeNamingConversition());
      }

      log.warn("Couldn't determine DB Dialect for {}", name);

      log.info("--->> database: setup dialect:{}", dialect.getClass().getSimpleName());
      return dialect;
    });
  }

  @Bean({ "jacksonObjectMapper", "objectMapper" })
  @Primary
  @ConditionalOnMissingBean
  public ObjectMapper objectMapper() {
    log.info("--->> json: setup jackson ObjectMapper");
    var builder = JsonMapper.builder();
    builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    builder.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    builder.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    builder.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    builder.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    builder.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);

    var objectMapper = builder.build();

    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    objectMapper.registerModule(new JacksonCustomerModule());
    objectMapper.registerModule(new JsonDiscriminatorModule());
    return objectMapper;
  }

  @Bean
  @Lazy
  @ConditionalOnMissingBean
  public Gson gson() {
    log.info("--->> json: setup gson");
    return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  }

  @Bean
  @ConditionalOnMissingBean
  public ProjectionFactory projectionFactory() {
    log.info("--->> projection: setup projection factory");
    return new SpelAwareProxyProjectionFactory();
  }

  @Bean("dataConversionService")
  public ConversionService dataConversionService(ApplicationContext applicationContext) {
    log.info("--->> conversion: setup dataConversionService");
    var dataConversionService = new DefaultConversionService();
    ApplicationConversionService.addApplicationConverters(dataConversionService);

    var genericConverters = applicationContext.getBeansOfType(GenericConverter.class);
    for (var kv : genericConverters.entrySet()) {
      log.info("   -- register GenericConverter {}: {}", kv.getKey(), kv.getValue());
      dataConversionService.addConverter(kv.getValue());
    }
    return dataConversionService;
  }
}
