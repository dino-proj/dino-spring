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

package org.dinospring.commons.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.CheckForSigned;

import lombok.experimental.UtilityClass;

/**
 *
 * @author Cody LU
 * @date 2022-03-07 21:55:09
 */

@UtilityClass
public class BatchUtils {
  /**
   *
   * @param <E>
   * @param list
   * @param batchSize
   * @param consumer
   */
  public static <E> void executeBatch(Iterable<E> list, @CheckForSigned int batchSize,
      Consumer<Collection<E>> consumer) {
    if (batchSize <= 0) {
      throw new IllegalArgumentException("batchSize must not be less than one");
    }
    if (list == null) {
      return;
    }
    Iterator<E> it = list.iterator();
    List<E> subList = new ArrayList<>(batchSize);
    while (it.hasNext()) {
      subList.add(it.next());
      if (subList.size() == batchSize) {
        consumer.accept(subList);
        subList.clear();
      }
    }
    if (!subList.isEmpty()) {
      consumer.accept(subList);
      subList.clear();
    }
  }
}
