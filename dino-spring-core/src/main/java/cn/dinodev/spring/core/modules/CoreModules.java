// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Cody Lu
 * @date 2022-03-31 23:19:31
 */

public class CoreModules {
  private static final Map<ModuleType, Boolean> MODULES_ENABLED = new EnumMap<>(ModuleType.class);

  public static void useModuleWallet() {
    MODULES_ENABLED.put(ModuleType.WALLET, true);
  }

  enum ModuleType {
    // 钱包模块
    WALLET
  }
}
