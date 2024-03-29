// Copyright 2021 dinodev.cn
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
import org.dinospring.commons.context.DinoContextThreadLocalImpl;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 */

@Slf4j
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class DinoCommonsAutoConfiguration implements ApplicationContextAware {

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (ContextHelper.getApplicationContext() == null) {
      log.info("--->> setup ContextHelper with applicationContext[id={}]", applicationContext.getId());
      ContextHelper.setApplicationContext(applicationContext);
    }

  }

  @Bean
  @ConditionalOnMissingBean
  public DinoContext dinoContext() {
    log.info("--->> use defalut dinoContext[class={}]", DinoContextThreadLocalImpl.class);
    return new DinoContextThreadLocalImpl();
  }

  @Bean
  @Lazy(false)
  @ConditionalOnMissingBean
  public ContextHelper contextHelper(DinoContext dinoContext) {
    log.info("--->> setup ContextHelper with dinoContext[class={}]", dinoContext.getClass());
    ContextHelper.setDinoContext(dinoContext);

    return ContextHelper.INST;
  }
}
