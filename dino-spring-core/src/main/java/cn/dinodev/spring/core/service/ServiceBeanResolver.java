// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.service;

import cn.dinodev.spring.commons.bean.BeanSafeCache;
import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.utils.TypeUtils;

import jakarta.annotation.Nonnull;

/**
 *
 * @author Cody Lu
 * @date 2022-05-31 19:38:06
 */

public interface ServiceBeanResolver<S extends ServiceBase<?, ?>> {

  static final BeanSafeCache<Service<?, ?>> SERVICE_MAPPING_CACHE = new BeanSafeCache<>();

  /**
  * Service 服务实例
  * @return
  */
  @Nonnull
  default S service() {
    var service = SERVICE_MAPPING_CACHE.getOrElse(this.getClass(),
        cls -> ContextHelper.findBean(TypeUtils.getGenericParamClass(cls, ServiceBeanResolver.class, 0)));
    return TypeUtils.cast(service);
  }
}