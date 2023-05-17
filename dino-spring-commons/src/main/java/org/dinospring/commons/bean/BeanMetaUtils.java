// Copyright 2022 dinodev.cn
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

package org.dinospring.commons.bean;

import lombok.experimental.UtilityClass;

/**
 *
 * @author tuuboo
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
