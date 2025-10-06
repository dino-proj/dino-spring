// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.scope;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dinodev.spring.core.modules.scope.ScopeRuleMatcher.HIT;
import cn.dinodev.spring.core.service.impl.ServiceBase;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;

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
      var entity = new ScopeEntity();
      entity.setScopeRule(rule);
      entity.setRuleHash(hash);
      beforeSaveEntity(entity);
      return scopeRepository.save(entity).getId();
    });
  }

  /**
   * 根据匹配器查找命中的范围规则ID列表
   *
   * @param <T> 规则类型
   * @param matcher 规则匹配器
   * @param ruleClass 规则类的Class对象
   * @return 匹配成功的范围ID列表
   */
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
