// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.autoconfig;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.utils.NamingUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Slf4j
@Configuration
@Profile("!prod")
public class OpenApiAutoConfiguration {

  @Value("${spring.application.name}")
  private String apiName;

  @Value("${spring.application.version}")
  private String apiVersion;

  @Value("${spring.application.description}")
  private String apiDescription;

  @Bean
  public OpenApiCustomizer openApiCustomiser(ObjectProvider<Info> infoProvider, ObjectProvider<Contact> contactProvider,
      ObjectProvider<License> licenseProvider, ObjectProvider<Server> serverProvider) {
    log.info("--->> api-doc: add info, contact:{}, license:{}", contactProvider.getIfAvailable(),
        licenseProvider.getIfAvailable());
    return openApi -> {
      var info = openApi.getInfo();
      if (Objects.isNull(info)) {
        info = infoProvider.getIfAvailable();
      }
      if (Objects.isNull(info)) {
        info = new Info()
            .title(StringUtils.capitalize(this.apiName) + " Open API")
            .description(Objects.toString(this.apiDescription, "开放API"))
            .version(this.apiVersion);

      }
      if (Objects.isNull(info.getContact())) {
        info.setContact(contactProvider.getIfAvailable());
      }
      if (Objects.isNull(info.getLicense())) {
        info.setLicense(licenseProvider.getIfAvailable());
      }
      openApi.setInfo(info);

      serverProvider.forEach(openApi::addServersItem);

      log.info("--->> snake schema property");
      if (openApi.getComponents() != null && openApi.getComponents().getSchemas() != null) {
        openApi.getComponents().getSchemas().values().forEach(this::snakeSchemaProperties);
      }
    };
  }

  @SuppressWarnings("rawtypes")
  private void snakeSchemaProperties(Schema<?> schema) {
    if (schema.getProperties() != null) {
      var kvs = schema.getProperties().entrySet().iterator();
      Map<String, Schema> newProps = new LinkedHashMap<>();
      while (kvs.hasNext()) {
        var kv = kvs.next();
        var newKey = NamingUtils.toSnake(kv.getKey());
        newProps.put(newKey, kv.getValue());
      }
      schema.setProperties(newProps);
    }
  }
}
