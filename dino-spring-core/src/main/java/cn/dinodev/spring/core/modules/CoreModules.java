// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules;

import java.util.EnumMap;
import java.util.Map;

/**
 * 核心模块管理工具类
 * @author Cody Lu
 * @date 2022-03-31 23:19:31
 */

public final class CoreModules {
  private static final Map<ModuleType, Boolean> MODULES_ENABLED = new EnumMap<>(ModuleType.class);

  /**
   * 私有构造函数，防止实例化
   */
  private CoreModules() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * 启用钱包模块
   */
  public static void useModuleWallet() {
    MODULES_ENABLED.put(ModuleType.WALLET, true);
  }

  /**
   * 模块类型枚举
   */
  enum ModuleType {
    /**
     * 钱包模块
     */
    WALLET
  }
}
