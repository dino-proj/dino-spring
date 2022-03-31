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

package org.dinospring.core.modules.scope;

import java.util.ArrayList;
import java.util.List;

import org.dinospring.core.modules.scope.ScopeRuleMatcher.HIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tuuboo
 * @date 2022-03-30 17:15:15
 */

@Service
public class ScopeService {

  @Autowired
  private ScopeRepository scopeRepository;

  /**
   * 保存规则，并返回规则ID
   * @param rule
   * @return
   */
  public Long saveRule(ScopeRule rule) {
    var hash = rule.hash();
    return scopeRepository.getByRuleHash(hash).orElseGet(() -> {
      var e = new ScopeEntity();
      e.setScopeRule(rule);
      e.setRuleHash(hash);
      return scopeRepository.saveAndFlush(e).getId();
    });
  }

  public <T> List<Long> hit(ScopeRuleMatcher<T> matcher, Class<T> ruleClass) {
    var sql = scopeRepository.newSelect();
    sql.columns("id, scope_rule");
    var maps = scopeRepository.queryForMap(sql, "id", Long.class, ruleClass);
    var matched = new ArrayList<Long>();
    maps.forEach((id, rule) -> {
      if (matcher.test(rule) == HIT.ACCEPT) {
        matched.add(id);
      }
    });
    return matched;
  }
}
