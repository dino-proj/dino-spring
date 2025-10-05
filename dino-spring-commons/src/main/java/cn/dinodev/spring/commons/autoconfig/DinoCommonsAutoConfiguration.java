// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.autoconfig;

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
 * Dino Commons自动配置类，配置通用组件和服务
 *
 * @author Cody Lu
 */

@Slf4j
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class DinoCommonsAutoConfiguration implements ApplicationContextAware {

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
    if (ContextHelper.getApplicationContext() == null) {
      log.info("--->> setup ContextHelper with applicationContext[id={}]", applicationContext.getId());
      ContextHelper.setApplicationContext(applicationContext);
    }

  }

  /**
   * 创建默认的DinoContext实例。
   * <p>
   * 当容器中没有DinoContext Bean时，创建一个基于ThreadLocal的默认实现。
   * </p>
   *
   * @return DinoContext实例
   */
  @Bean
  @ConditionalOnMissingBean
  public DinoContext dinoContext() {
    log.info("--->> use defalut dinoContext[class={}]", DinoContextThreadLocalImpl.class);
    return new DinoContextThreadLocalImpl();
  }

  /**
   * 创建ContextHelper Bean并进行初始化配置。
   * <p>
   * 设置ContextHelper使用的DinoContext实例，并返回ContextHelper的单例。
   * 该Bean会立即初始化（非懒加载）以确保ContextHelper尽早可用。
   * </p>
   *
   * @param dinoContext DinoContext实例
   * @return ContextHelper单例
   */
  @Bean
  @Lazy(false)
  @ConditionalOnMissingBean
  public ContextHelper contextHelper(DinoContext dinoContext) {
    log.info("--->> setup ContextHelper with dinoContext[class={}]", dinoContext.getClass());
    ContextHelper.setDinoContext(dinoContext);

    return ContextHelper.INST;
  }
}
