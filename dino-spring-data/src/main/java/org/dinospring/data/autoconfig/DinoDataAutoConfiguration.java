// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.autoconfig;

import org.dinospring.commons.autoconfig.DinoCommonsAutoConfiguration;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.json.JsonDiscriminatorModule;
import org.dinospring.data.converts.JacksonCustomerModule;
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

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
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
    Assert.notNull(this.contextHelper, "contextHelper should init before DinoDataAutoConfiguration");
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
