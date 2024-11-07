// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.bean;

import lombok.experimental.UtilityClass;

/**
 *
 * @author Cody Lu
 * @date 2022-05-28 04:26:05
 */

@UtilityClass
public class BeanMetaUtils {

  private static final BeanSafeCache<BeanMeta> BEANINFO_CACHE = new BeanSafeCache<>();

  /**
   * the bean info of the bean class
   * @param beanClass
   * @return
   */
  public static BeanMeta forClass(Class<?> beanClass) {
    return BEANINFO_CACHE.getOrElse(beanClass, BeanMetaImpl::new);
  }

  /**
   * the bean info of the bean class, with active json view
   * @see com.fasterxml.jackson.annotation.JsonView
   *
   * @param beanClass
   * @param activeView
   * @return
   */
  public static BeanMetaWithJsonView forClassWithJsonView(Class<?> beanClass, Class<?> activeView) {
    var beanMeta = forClass(beanClass);
    return new BeanMetaWithJsonView(beanMeta, activeView);
  }
}
