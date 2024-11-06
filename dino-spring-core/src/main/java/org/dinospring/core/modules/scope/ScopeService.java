// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.scope;

import java.util.ArrayList;
import java.util.List;

import org.dinospring.core.modules.scope.ScopeRuleMatcher.HIT;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cody Lu
 * @date 2022-03-30 17:15:15
 */

@Service
public class ScopeService extends ServiceBase<ScopeEntity, Long> {

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
      beforeSaveEntity(e);
      return scopeRepository.save(e).getId();
    });
  }

  public <T> List<Long> hit(ScopeRuleMatcher<T> matcher, Class<T> ruleClass) {
    var sql = scopeRepository.newSelect();
    var maps = scopeRepository.queryForMap(sql, "id", Long.class, "scope_rule", ruleClass);
    var matched = new ArrayList<Long>();
    maps.forEach((id, rule) -> {
      if (matcher.test(rule) == HIT.ACCEPT) {
        matched.add(id);
      }
    });
    return matched;
  }

  @Override
  public CrudRepositoryBase<ScopeEntity, Long> repository() {
    return scopeRepository;
  }
}
