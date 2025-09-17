// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.scope.rule;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import cn.dinodev.spring.core.modules.scope.ScopeRuleMatcher;

/**
 *
 * @author Cody Lu
 * @date 2022-03-31 16:45:30
 */

public class AnyMatcher<T extends Serializable & Comparable<T>> implements ScopeRuleMatcher<IncludeExcludeRule<T>> {

  private final List<T> values;

  public AnyMatcher(List<T> values) {
    this.values = values;
  }

  @Override
  public HIT test(IncludeExcludeRule<T> rule) {
    if (CollectionUtils.isEmpty(values) || Objects.isNull(rule)) {
      return HIT.MISS;
    }
    if (CollectionUtils.isNotEmpty(rule.getExclude()) && CollectionUtils.containsAny(rule.getExclude(), values)) {
      return HIT.REJECT;
    }
    if (CollectionUtils.isNotEmpty(rule.getInclude()) && CollectionUtils.containsAny(rule.getInclude(), values)) {
      return HIT.ACCEPT;
    }
    return HIT.MISS;
  }

}
