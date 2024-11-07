// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.scope;

/**
 *
 * @author Cody Lu
 * @date 2022-03-31 16:52:09
 */

public interface ScopeRuleMatcher<T> {

  /**
   * 判断规则是否命中
   * @param rule 规则
   * @return
   */
  HIT test(T rule);

  public enum HIT {
    //不命中
    MISS,
    //命中，且通过
    ACCEPT,
    //命中，且拒绝
    REJECT
  }
}
