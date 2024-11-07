// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.context;

import java.io.Serializable;

import org.apache.commons.collections4.MapUtils;
import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.InheritableThreadLocalMap;
import cn.dinodev.spring.commons.utils.TypeUtils;
import org.springframework.context.ApplicationContext;

/**
 * {@link #DinoContext} 的ThreadLocal版本的实现
 * @author Cody Lu
 */
public class DinoContextThreadLocalImpl implements DinoContext {
  private static final InheritableThreadLocalMap RESOURCES = new InheritableThreadLocalMap();
  private static final String KEY_CURRENT_TENANT = DinoContextThreadLocalImpl.class.getName() + "_CURRENT_TENANT";
  private static final String KEY_CURRENT_USER = DinoContextThreadLocalImpl.class.getName() + "_CURRENT_USER";

  private static ApplicationContext applicationContext;

  public static Tenant getCurrentTenant() {
    return TypeUtils.cast(MapUtils.getObject(RESOURCES.get(), KEY_CURRENT_TENANT));
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
