// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.auth.annotation;

/**
 * 逻辑模式
 * @author Cody Lu
 * @date 2022-04-06 22:39:21
 */

public enum Logic {
  // 所有条件都需满足
  ALL,
  // 任意条件满足即可
  ANY;
}
