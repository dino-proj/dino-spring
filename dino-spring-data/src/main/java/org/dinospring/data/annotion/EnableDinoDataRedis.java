// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dinospring.data.autoconfig.DinoDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * Annotion for Enable Dino Spring Data Redis
 * @author Cody Lu
 * @date 2023-12-26 23:23:14
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableRedisRepositories(// config redis
    includeFilters = @Filter(// start filter
        type = FilterType.ASSIGNABLE_TYPE, // type
        classes = org.springframework.data.keyvalue.repository.KeyValueRepository.class // interface
    )// end filter
)
@Import({ DinoDataAutoConfiguration.class })
public @interface EnableDinoDataRedis {
  /**
   * Base packages to scan for annotated components. please include
   * "org.dinospring"
   */
  @AliasFor(annotation = EnableRedisRepositories.class, attribute = "basePackages")
  String[] basePackages() default { "org.dinospring" };
}
