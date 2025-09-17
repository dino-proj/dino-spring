// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.context;

import java.io.Serializable;

import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.commons.sys.User;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 *
 * @author Cody Lu
 */
public class ContextHelper {

  public static final ContextHelper INST = new ContextHelper();

  private static DinoContext context;

  private static ApplicationContext applicationContext;

  private ContextHelper() {
  }

  public static void setApplicationContext(ApplicationContext context) {
    applicationContext = context;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static void setDinoContext(DinoContext dinoAppContext) {
    context = dinoAppContext;
  }

  public static DinoContext getDinoContext() {
    return context;
  }

  public static String currentTenantId() {
    Assert.notNull(context, "DinoContext bean not found");
    return context.currentTenant() == null ? null : context.currentTenant().getId();
  }

  public static Tenant currentTenant() {
    Assert.notNull(context, "DinoContext bean not found");
    return context.currentTenant();
  }

  public static <K extends Serializable> User<K> currentUser() {
    Assert.notNull(context, "DinoContext bean not found");
    return context.currentUser();
  }

  /**
   * 查找Bean
   * @param <T>
   * @param requiredType Bean的类型
   * @return 找不到返回{@code null}
   */
  public static <T> T findBean(Class<T> requiredType) {
    return applicationContext.getBean(requiredType);
  }

  /**
   * 查找Bean
   * @param <T>
   * @param name Bean的名字
   * @param requiredType Bean的类型
   * @return 找不到返回{@code null}
   */
  public static <T> T findBean(String name, Class<T> requiredType) {
    return applicationContext.getBean(name, requiredType);
  }

}
