// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.scope.rule;

import org.apache.commons.collections4.CollectionUtils;
import cn.dinodev.spring.core.modules.scope.ScopeRuleMatcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Cody Lu
 * @date 2022-03-31 16:47:07
 */

public class TreeMatcher<T extends Serializable & Comparable<T>> implements ScopeRuleMatcher<IncludeExcludeRule<T>> {

  private final List<T> reversePaths;

  public TreeMatcher(List<T> paths) {
    if (CollectionUtils.isEmpty(paths)) {
      reversePaths = Collections.emptyList();
    } else {
      reversePaths = new ArrayList<>(paths);
      Collections.reverse(reversePaths);
    }
  }

  @SafeVarargs
  public TreeMatcher(T... paths) {
    this(Arrays.asList(paths));

  }

  @Override
  public HIT test(IncludeExcludeRule<T> rule) {
    if (Objects.isNull(rule)) {
      return HIT.MISS;
    }
    for (T value : reversePaths) {
      if (CollectionUtils.isNotEmpty(rule.getExclude()) && Collections.binarySearch(rule.getExclude(), value) >= 0) {
        return HIT.REJECT;
      }
      if (CollectionUtils.isNotEmpty(rule.getInclude()) && Collections.binarySearch(rule.getInclude(), value) >= 0) {
        return HIT.ACCEPT;
      }
    }
    return HIT.MISS;
  }

}
