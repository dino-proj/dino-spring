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

package org.dinospring.commons.autoconfig;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.context.DinoContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 */

@Slf4j
@Configuration
@Order(1)
public class DinoCommonsAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ContextHelper contextHelper(DinoContext dinoContext) {
    log.info("setup ContextHelper with dinoContext[class={}]", dinoContext.getClass());
    ContextHelper.setDinoContext(dinoContext);

    return ContextHelper.INST;
  }
}