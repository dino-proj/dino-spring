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

package org.dinospring.data.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dinospring.data.autoconfig.CustomJpaRepositoryFactoryBean;
import org.dinospring.data.dao.impl.JdbcSelectExecutorImpl;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableJpaRepositories(basePackages = "org.dinospring", repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class, repositoryBaseClass = JdbcSelectExecutorImpl.class)
public @interface EnableDinoData {
  /**
   * Base packages to scan for annotated components. please include "org.dinospring"
   */
  @AliasFor(annotation = EnableJpaRepositories.class, attribute = "basePackages")
  String[] basePackages() default { "org.dinospring" };
}
