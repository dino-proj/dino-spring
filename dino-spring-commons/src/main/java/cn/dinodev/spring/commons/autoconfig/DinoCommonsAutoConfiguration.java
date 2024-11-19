// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.autoconfig;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.context.DinoContext;
import cn.dinodev.spring.commons.context.DinoContextThreadLocalImpl;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Slf4j
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class DinoCommonsAutoConfiguration implements ApplicationContextAware {

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
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
