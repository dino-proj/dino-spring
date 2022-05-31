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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.utils.NamingUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 */

@Slf4j
@Configuration
public class OpenApiAutoConfiguration {

  @Value("${spring.application.name}")
  private String apiName;

  @Value("${spring.application.version}")
  private String apiVersion;

  @Value("${spring.application.description}")
  private String apiDescription;

  @Bean
  public OpenApiCustomiser openApiCustomiser() {
    log.info("--->> customize api-doc info");
    return openApi -> {
      if (Objects.isNull(openApi.getInfo())) {
        openApi.info(new Info()
            .title(StringUtils.capitalize(apiName) + " Open API")
            .description(StringUtils.defaultString(apiDescription, "开放API"))
            .version(apiVersion));
      }
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

  public static class JsonModelConverter implements ModelConverter {

    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
      log.warn("type:{} context:{}", type, context);
      return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
    }

  }
}
