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

package org.dinospring.core.modules.scope.rule;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.core.modules.scope.ScopeRuleMatcher;

/**
 *
 * @author Cody LU
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
