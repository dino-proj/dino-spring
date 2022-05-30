// Copyright 2022 dinospring.cn
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
public class BeanInfoUtils {

  private static final BeanSafeCache<BeanInfo> BEANINFO_CACHE = new BeanSafeCache<>();

  private static final BeanSafeCache<BeanInfoWithJsonView> BEANINFO_JSONVIEW_CACHE = new BeanSafeCache<>();

  /**
   * the bean info of the bean class
   * @param beanClass
   * @return
   */
  public static BeanInfo forClass(Class<?> beanClass) {
    return BEANINFO_CACHE.getOrElse(beanClass, (bc) -> new BeanInfo(bc));
  }

  /**
   * the bean info of the bean class, with active json view
   * @see com.fasterxml.jackson.annotation.JsonView
   *
   * @param beanClass
   * @param activeView
   * @return
   */
  public static BeanInfoWithJsonView forClassWithJsonView(Class<?> beanClass, Class<?> activeView) {
    return BEANINFO_JSONVIEW_CACHE.getOrElse(beanClass, (bc) -> new BeanInfoWithJsonView(bc, activeView));
  }
}
