// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import lombok.experimental.UtilityClass;

/**
 *
 * @author Cody Lu
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
  public static <E> void executeBatch(Iterable<E> list, int batchSize,
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
