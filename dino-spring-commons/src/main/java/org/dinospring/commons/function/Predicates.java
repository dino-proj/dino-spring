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

package org.dinospring.commons.function;

import java.util.Collection;
import java.util.function.Predicate;

/**
 *
 * @author tuuboo
 * @date 2022-04-08 14:42:03
 */

public interface Predicates {

  /**
   * “与”预测器
   *
   * @param components
   * @return
   */
  static <T> Predicate<T> and(Collection<Predicate<T>> components) {
    return t -> {
      for (Predicate<T> component : components) {
        if (!component.test(t)) {
          return false;
        }
      }
      return true;
    };
  }

  /**
   * “或”预测器
   *
   * @param components
   * @return
   */
  static <T> Predicate<T> or(Collection<Predicate<T>> components) {
    return t -> {
      for (Predicate<T> component : components) {
        if (component.test(t)) {
          return true;
        }
      }
      return false;
    };
  }

  /**
   * “非”预测器
   *
   * @param component
   * @return
   */
  static <T> Predicate<T> not(Predicate<T> component) {
    return t -> !component.test(t);
  }

  /**
   * 总是返回true的预测器
   *
   * @param <T>
   * @return
   */
  static <T> Predicate<T> alwaysTrue() {
    return t -> true;
  }

  /**
   * 总是返回false的预测器
   *
   * @param <T>
   * @return
   */
  static <T> Predicate<T> alwaysFalse() {
    return t -> false;
  }

  /**
   * 判断是否包含指定的值
   *
   * @param values
   * @return
   */
  static <T> Predicate<T> in(Collection<T> values) {
    return values::contains;
  }

  /**
   * "hasAny"预测器
   *
   * @param values
   * @return
   */

  static <T extends Comparable<T>> Predicate<Collection<T>> hasAny(T... values) {
    return t -> {
      for (T value : values) {
        if (t.contains(value)) {
          return true;
        }
      }
      return false;
    };
  }

  /**
   * "hasAll"预测器
   *
   * @param values
   * @return
   */
  static <T extends Comparable<T>> Predicate<Collection<T>> hasAll(T... values) {
    return t -> {
      for (T value : values) {
        if (!t.contains(value)) {
          return false;
        }
      }
      return true;
    };
  }

  /**
   * 判断是否不包含指定的值
   *
   * @param values
   * @return
   */
  static <T> Predicate<T> notIn(Collection<T> values) {
    return t -> !values.contains(t);
  }

  /**
   * 判断是否为空
   *
   * @param <T>
   * @return
   */
  static <T extends CharSequence> Predicate<T> isEmpty() {
    return t -> t == null || t.length() == 0;
  }

  /**
   * 判断是否不为空
   *
   * @param <T>
   * @return
   */
  static <T extends CharSequence> Predicate<T> isNotEmpty() {
    return t -> t != null && t.length() > 0;
  }
}
