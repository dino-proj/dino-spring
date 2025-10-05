// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.context;

import java.io.Serializable;

import org.apache.commons.collections4.MapUtils;
import org.springframework.context.ApplicationContext;

import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.InheritableThreadLocalMap;
import cn.dinodev.spring.commons.utils.TypeUtils;

/**
 * {@link DinoContext} 的ThreadLocal版本的实现
 *
 * @author Cody Lu
 */
public class DinoContextThreadLocalImpl implements DinoContext {
  private static final InheritableThreadLocalMap RESOURCES = new InheritableThreadLocalMap();
  private static final String KEY_CURRENT_TENANT = DinoContextThreadLocalImpl.class.getName() + "_CURRENT_TENANT";
  private static final String KEY_CURRENT_USER = DinoContextThreadLocalImpl.class.getName() + "_CURRENT_USER";

  private static ApplicationContext applicationContext;

  /**
   * 获取当前线程的租户信息。
   * <p>
   * 从ThreadLocal存储中获取当前线程关联的租户对象。
   * </p>
   *
   * @return 当前租户信息，可能为null
   */
  public static Tenant getCurrentTenant() {
    return TypeUtils.cast(MapUtils.getObject(RESOURCES.get(), KEY_CURRENT_TENANT));
  }

  /**
   * 设置当前线程的租户信息。
   * <p>
   * 将租户信息存储到ThreadLocal中，如果传入null则移除租户信息。
   * </p>
   *
   * @param tenant 要设置的租户信息，null表示清除
   */
  public static void setCurrentTenant(Tenant tenant) {
    if (tenant == null) {
      RESOURCES.remove(KEY_CURRENT_TENANT);
    } else {
      RESOURCES.put(KEY_CURRENT_TENANT, tenant);
    }
  }

  /**
   * 清除当前线程的所有上下文信息。
   * <p>
   * 移除当前线程中存储的所有资源信息，包括租户和用户信息。
   * </p>
   */
  public static void remove() {
    RESOURCES.remove();
  }

  /**
   * 获取当前线程的用户信息。
   * <p>
   * 从ThreadLocal存储中获取当前线程关联的用户对象。
   * </p>
   *
   * @param <T> 用户类型
   * @return 当前用户信息，可能为null
   */
  public static <T extends User<?>> T getCurrentUser() {
    return RESOURCES.get(KEY_CURRENT_USER);
  }

  /**
   * 设置当前线程的用户信息。
   * <p>
   * 将用户信息存储到ThreadLocal中，如果传入null则移除用户信息。
   * </p>
   *
   * @param <T> 用户类型
   * @param user 要设置的用户信息，null表示清除
   */
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
