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

package org.dinospring.core.modules.scope.rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.core.modules.scope.ScopeRuleMatcher;

/**
 *
 * @author tuuboo
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
    for (T value : reversePaths) {
      if (Collections.binarySearch(rule.getExclude(), value) >= 0) {
        return HIT.REJECT;
      }
      if (Collections.binarySearch(rule.getInclude(), value) >= 0) {
        return HIT.ACCEPT;
      }
    }
    return HIT.MISS;
  }

}
