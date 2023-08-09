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

package org.dinospring.core.annotion.binder;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.dinospring.commons.utils.LambdaUtils;
import org.dinospring.commons.utils.NamingUtils;
import org.dinospring.core.service.Service;

/**
 *
 * @author Cody LU
 */

public class Binder<E, V, K extends Serializable> {
  /**
  * 需要绑定到的VO注解对象List
  */
  protected List<V> voList;

  /**
   * VO注解对象中的join on对象属性集合
   */
  protected List<Pair<String, String>> joinOnFields;

  /**
   * 被关联对象的Service实例
   */
  protected Service<E, K> referencedService;

  /**
   * 被关联对象的EntityClass
   */
  protected Class<E> referencedEntityClass;

  /**
   * join连接条件，指定当前VO的取值方法和关联entity的取值方法
   * @param <T>
   * @param entityFieldGetter Entity的Getter方法
   * @param voFieldGetter VO的Getter方法
   * @return
   */
  public <T> Binder<E, V, K> joinOn(Function<E, T> entityFieldGetter, Function<V, T> voFieldGetter) {
    return joinOn(LambdaUtils.methodToProperty(entityFieldGetter), LambdaUtils.methodToProperty(voFieldGetter));
  }

  /**
   *
   * join连接条件，指定当前VO的取值方法和关联entity的取值方法
   * @param entityField Entity的Field名字
   * @param voField vo的Field名字
   * @return
   */
  public Binder<E, V, K> joinOn(String entityField, String voField) {
    if (StringUtils.isNotEmpty(entityField) && StringUtils.isNotEmpty(voField)) {
      joinOnFields.add(Pair.of(NamingUtils.toCamel(entityField), NamingUtils.toCamel(voField)));
    }
    return this;
  }

}
