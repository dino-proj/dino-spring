// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.autoconfig;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import cn.dinodev.spring.data.jdbc.DinoJdbcMappingContext;
import cn.dinodev.spring.data.jdbc.DinoJdbcSimpleTypeHolder;
import cn.dinodev.spring.data.jdbc.mapping.DinoJdbcCustomConversions;
import cn.dinodev.spring.data.sql.dialect.Dialect;
import cn.dinodev.spring.data.sql.dialect.MysqlDialect;
import cn.dinodev.spring.data.sql.dialect.PostgreSQLDialect;
import cn.dinodev.spring.data.sql.dialect.SnakeNamingConversition;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.core.mapping.JdbcSimpleTypes;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.util.TypeScanner;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;

import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 * @date 2023-05-18 00:19:57
 */

@Configuration(proxyBeanMethods = false)
@Slf4j
public class DinoDataJdbcConfiguration extends AbstractJdbcConfiguration {

  private ApplicationContext applicationContext;

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

  @Override
  public JdbcMappingContext jdbcMappingContext(Optional<NamingStrategy> namingStrategy,
      JdbcCustomConversions customConversions, RelationalManagedTypes jdbcManagedTypes) {
    var mappingContext = new DinoJdbcMappingContext(namingStrategy.orElse(DefaultNamingStrategy.INSTANCE));
    mappingContext.setSimpleTypeHolder(new DinoJdbcSimpleTypeHolder(customConversions.getSimpleTypeHolder()));
    mappingContext.setManagedTypes(jdbcManagedTypes);
    return mappingContext;
  }

  @Override
  protected List<?> userConverters() {
    var readingConverters = this.applicationContext.getBeansWithAnnotation(ReadingConverter.class);
    var writingConverters = this.applicationContext.getBeansWithAnnotation(WritingConverter.class);

    var converts = new ArrayList<>(readingConverters.values().stream().toList());
    converts.addAll(writingConverters.values().stream().toList());
    return converts;
  }

  @Bean
  public JdbcCustomConversions jdbcCustomConversions() {

    try {

      var dialect = this.applicationContext.getBean(org.springframework.data.relational.core.dialect.Dialect.class);
      SimpleTypeHolder simpleTypeHolder = dialect.simpleTypes().isEmpty() ? JdbcSimpleTypes.HOLDER
          : new SimpleTypeHolder(dialect.simpleTypes(), JdbcSimpleTypes.HOLDER);

      return new DinoJdbcCustomConversions(
          CustomConversions.StoreConversions.of(simpleTypeHolder, this.storeConverters(dialect)),
          this.userConverters());

    } catch (NoSuchBeanDefinitionException exception) {

      log.warn("No dialect found; CustomConversions will be configured without dialect specific conversions");

      return new JdbcCustomConversions();
    }
  }

  private List<Object> storeConverters(org.springframework.data.relational.core.dialect.Dialect dialect) {

    List<Object> converters = new ArrayList<>();
    converters.addAll(dialect.getConverters());
    converters.addAll(JdbcCustomConversions.storeConverters());
    return converters;
  }

  @Override
  protected Set<Class<?>> scanForEntities(String basePackage) {
    var fromSuper = super.scanForEntities(basePackage);

    if (StringUtils.isBlank(basePackage)) {
      return fromSuper;
    }

    @SuppressWarnings("unchecked")
    var jakartaTables = TypeScanner.typeScanner(AbstractJdbcConfiguration.class.getClassLoader()) //
        .forTypesAnnotatedWith(Table.class) //
        .scanPackages(basePackage) //
        .collectAsSet(); // Explicitly specify the type of the array

    jakartaTables.addAll(fromSuper);
    return jakartaTables;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    super.setApplicationContext(applicationContext);
  }

}
