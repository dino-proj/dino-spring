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
package org.dinospring.commons.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.util.CastUtils;

/**
 * InheritableThreadLocalMap
 * @author tuuboo
 * @date 2022-04-08 17:58:00
 */

public class InheritableThreadLocalMap extends InheritableThreadLocal<Map<String, Object>> {

  /**
   * @param parentValue the parent value, a HashMap as defined in the {@link #initialValue()} method.
   * @return the HashMap to be used by any parent-spawned child threads (a clone of the parent HashMap).
   */
  @Override
  protected Map<String, Object> childValue(Map<String, Object> parentValue) {
    return ObjectUtils.clone(parentValue);
  }

  private synchronized void ensureResourcesInitialized() {
    if (this.get() == null) {
      this.set(new HashMap<>(4));
    }
  }

  /**
   * 添加资源
   * @param key 键值
   * @param value 值
   */
  public void put(String key, Object value) {
    ensureResourcesInitialized();
    get().put(key, value);
  }

  /**
   * 获取资源
   * @param key 键值
   * @return 值
   */
  public <T> T get(String key) {
    ensureResourcesInitialized();
    return CastUtils.cast(get().get(key));
  }

  /**
   * 获取资源，如果不存在则返回默认值
   * @param key 键值
   * @param defaultValue 默认值
   */
  public <T> T getOrDefault(String key, T defaultValue) {
    ensureResourcesInitialized();
    return CastUtils.cast(get().getOrDefault(key, defaultValue));
  }

  /**
   * 移除资源
   * @param key 键值
   */
  public void remove(String key) {
    ensureResourcesInitialized();
    get().remove(key);
  }

  /**
   * 清空资源
   */
  public void clear() {
    set(null);
  }

}
