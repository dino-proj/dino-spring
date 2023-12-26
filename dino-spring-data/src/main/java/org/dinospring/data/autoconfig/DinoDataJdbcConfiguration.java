// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.autoconfig;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.data.converts.PostgreJsonbReadingConverter;
import org.dinospring.data.converts.PostgreJsonbWritingConverter;
import org.dinospring.data.sql.dialect.Dialect;
import org.dinospring.data.sql.dialect.MysqlDialect;
import org.dinospring.data.sql.dialect.PostgreSQLDialect;
import org.dinospring.data.sql.dialect.SnakeNamingConversition;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.DialectResolver;
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
    mappingContext.setSimpleTypeHolder(new DinoSimpleTypeHolder(customConversions.getSimpleTypeHolder()));
    mappingContext.setManagedTypes(jdbcManagedTypes);

    return mappingContext;
  }

  private final JdbcOperations operations;

  public DinoDataJdbcConfiguration(JdbcOperations operations) {
    this.operations = operations;
  }

  @Override
  protected List<?> userConverters() {
    var genericConverters = this.applicationContext.getBeansOfType(PostgreJsonbReadingConverter.class);
    return genericConverters.values().stream().toList();
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

  @Override
  public JdbcCustomConversions jdbcCustomConversions() {
    // 获取当前数据库方言
    var dialect = DialectResolver.getDialect(this.operations);
    // 获取默认转换器
    List<Object> defaultConverters = new ArrayList<>(dialect.getConverters());
    defaultConverters.addAll(JdbcCustomConversions.storeConverters());
    defaultConverters
        .addAll(this.applicationContext.getBeansOfType(PostgreJsonbWritingConverter.class).values().stream().toList());

    // 创建并返回包含默认转换器和自定义转换器的JdbcCustomConversions
    return new JdbcCustomConversions(defaultConverters);
  }
}
