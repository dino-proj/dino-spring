// Copyright 2023 dinodev.cn
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.data.converts.PostgreJsonbReadingConverter;
import org.dinospring.data.converts.PostgreJsonbWritingConverter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.DialectResolver;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.util.TypeScanner;
import org.springframework.jdbc.core.JdbcOperations;

/**
 *
 * @author Cody Lu
 * @date 2023-05-18 00:19:57
 */

@Configuration(proxyBeanMethods = false)
public class DinoDataJdbcConfiguration extends AbstractJdbcConfiguration {

  private ApplicationContext applicationContext;

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
    var genericConverters = applicationContext.getBeansOfType(PostgreJsonbReadingConverter.class);
    return genericConverters.values().stream().toList();
  }

  @Override
  protected Set<Class<?>> scanForEntities(String basePackage) {
    if (StringUtils.isBlank(basePackage)) {
      return Collections.emptySet();
    }

    return TypeScanner.typeScanner(AbstractJdbcConfiguration.class.getClassLoader()) //
        .forTypesAnnotatedWith(jakarta.persistence.Table.class,
            org.springframework.data.relational.core.mapping.Table.class) //
        .scanPackages(basePackage) //
        .collectAsSet();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    super.setApplicationContext(applicationContext);
  }

  @Override
  public JdbcCustomConversions jdbcCustomConversions() {
    // 获取当前数据库方言
    Dialect dialect = DialectResolver.getDialect(operations);
    // 获取默认转换器
    List<Object> defaultConverters = new ArrayList<>(dialect.getConverters());
    defaultConverters.addAll(JdbcCustomConversions.storeConverters());
    defaultConverters
        .addAll(applicationContext.getBeansOfType(PostgreJsonbWritingConverter.class).values().stream().toList());

    // 创建并返回包含默认转换器和自定义转换器的JdbcCustomConversions
    return new JdbcCustomConversions(defaultConverters);
  }

}
