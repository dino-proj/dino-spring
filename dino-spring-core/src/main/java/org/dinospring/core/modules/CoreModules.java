package org.dinospring.core.modules;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Cody LU
 * @date 2022-03-31 23:19:31
 */

public class CoreModules {
  private static final Map<ModuleType, Boolean> MODULES_ENABLED = new HashMap<>();

  public static void useModuleWallet() {
    MODULES_ENABLED.put(ModuleType.WALLET, true);
  }

  enum ModuleType {
    // 钱包模块
    WALLET
  }
}
