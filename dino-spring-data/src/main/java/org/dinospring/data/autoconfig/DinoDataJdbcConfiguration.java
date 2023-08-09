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

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;

/**
 *
 * @author Cody Lu
 * @date 2023-05-18 00:19:57
 */

@Configuration(proxyBeanMethods = false)
public class DinoDataJdbcConfiguration extends AbstractJdbcConfiguration {

  @Override
  public JdbcMappingContext jdbcMappingContext(Optional<NamingStrategy> namingStrategy,
      JdbcCustomConversions customConversions, RelationalManagedTypes jdbcManagedTypes) {
    var mappingContext = new DinoJdbcMappingContext(namingStrategy.orElse(DefaultNamingStrategy.INSTANCE));
    mappingContext.setSimpleTypeHolder(new DinoSimpleTypeHolder(customConversions.getSimpleTypeHolder()));

    return mappingContext;
  }

}
