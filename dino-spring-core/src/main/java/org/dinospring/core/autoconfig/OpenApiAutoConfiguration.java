package org.dinospring.core.autoconfig;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;

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
  public OpenAPI customOpenAPI() {
    log.info("start custom open api info");
    return new OpenAPI()//
        .info(new Info()//
            .title(StringUtils.capitalize(apiName) + " Open API")//
            .description(StringUtils.defaultString(apiDescription, "布本智能，开放API"))//
            .version(apiVersion)
            .contact(new Contact().name("botbrain").email("luxueyu@botbrain.ai").url("https://botbrain.ai")));
  }

  public static class JsonModelConverter implements ModelConverter {

    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
      log.warn("type:{} context:{}", type, context);
      return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
    }

  }
}
