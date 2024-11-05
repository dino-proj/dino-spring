// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.controller;

import java.io.Serializable;

import jakarta.annotation.Nonnull;

import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.service.ServiceBase;
import org.dinospring.core.service.ServiceBeanResolver;
import org.dinospring.core.vo.VoBase;
import org.dinospring.data.domain.EntityBase;

/**
 *
 * @author Cody LU
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
