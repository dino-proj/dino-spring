// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.controller;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import cn.dinodev.spring.commons.utils.TypeUtils;
import cn.dinodev.spring.core.service.ServiceBase;
import cn.dinodev.spring.core.service.ServiceBeanResolver;
import cn.dinodev.spring.core.vo.VoBase;
import cn.dinodev.spring.data.domain.EntityBase;

/**
 *
 * @author Cody Lu
 */

public interface ControllerBase<S extends ServiceBase<E, K>, E extends EntityBase<K>, VO extends VoBase<K>, K extends Serializable>
    extends ServiceBeanResolver<S> {

  /**
   * Vo类的Class
   * @return
   */
  @Nonnull
  default Class<VO> voClass() {
    return TypeUtils.getGenericParamClass(this, CrudControllerBase.class, 2);
  }

  /**
   * Entity类的Class
   * @return
   */
  @Nonnull
  default Class<E> entityClass() {
    return TypeUtils.getGenericParamClass(this, CrudControllerBase.class, 1);
  }

}
