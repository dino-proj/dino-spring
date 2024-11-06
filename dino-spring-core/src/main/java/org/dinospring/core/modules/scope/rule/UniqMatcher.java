// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.scope.rule;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;

import org.dinospring.core.modules.scope.ScopeRuleMatcher;

/**
 *
 * @author Cody Lu
 * @date 2022-03-31 16:47:48
 */

public class UniqMatcher<T extends Serializable & Comparable<T>> implements ScopeRuleMatcher<IncludeExcludeRule<T>> {

  private final T value;

  public UniqMatcher(T value) {
    Objects.requireNonNull(value);
    this.value = value;
  }

  @Override
  public HIT test(IncludeExcludeRule<T> rule) {
    if (Collections.binarySearch(rule.getExclude(), value) >= 0) {
      return HIT.REJECT;
    }
    if (Collections.binarySearch(rule.getInclude(), value) >= 0) {
      return HIT.ACCEPT;
    }
    return HIT.MISS;
  }

}
