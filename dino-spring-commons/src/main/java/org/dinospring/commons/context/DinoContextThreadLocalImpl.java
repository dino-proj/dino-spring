// Copyright 2021 dinospring.cn
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

package org.dinospring.commons.context;

import java.io.Serializable;

import org.apache.commons.collections4.MapUtils;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.commons.utils.InheritableThreadLocalMap;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.CastUtils;

/**
 * {@link #DinoContext} 的ThreadLocal版本的实现
 * @author tuuboo
 */
public class DinoContextThreadLocalImpl implements DinoContext {
  private static final InheritableThreadLocalMap RESOURCES = new InheritableThreadLocalMap();
  private static final String KEY_CURRENT_TENANT = DinoContextThreadLocalImpl.class.getName() + "_CURRENT_TENANT";
  private static final String KEY_CURRENT_USER = DinoContextThreadLocalImpl.class.getName() + "_CURRENT_USER";

  private static ApplicationContext applicationContext;

  public static Tenant getCurrentTenant() {
    return CastUtils.cast(MapUtils.getObject(RESOURCES.get(), KEY_CURRENT_TENANT));
  }

  public static void setCurrentTenant(Tenant tenant) {
    if (tenant == null) {
      RESOURCES.remove(KEY_CURRENT_TENANT);
    } else {
      RESOURCES.put(KEY_CURRENT_TENANT, tenant);
    }
  }

  public static void remove() {
    RESOURCES.remove();
  }

  public static <T extends User<?>> T getCurrentUser() {
    return RESOURCES.get(KEY_CURRENT_USER);
  }

  public static <T extends User<?>> void setCurrentUser(T user) {
    if (user == null) {
      RESOURCES.remove(KEY_CURRENT_USER);
    } else {
      RESOURCES.put(KEY_CURRENT_USER, user);
    }
  }

  public static void setApplicationContext(ApplicationContext context) {
    applicationContext = context;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public <K extends Serializable> User<K> currentUser() {
    return getCurrentUser();
  }

  @Override
  public Tenant currentTenant() {
    return getCurrentTenant();
  }

  @Override
  public <K extends Serializable> void currentUser(User<K> user) {
    setCurrentUser(user);
  }

  @Override
  public void currentTenant(Tenant tenant) {
    setCurrentTenant(tenant);
  }

}
