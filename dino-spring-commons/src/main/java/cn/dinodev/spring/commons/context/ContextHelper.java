// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.context;

import java.io.Serializable;

import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.commons.sys.User;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * 上下文帮助类，提供Spring应用上下文和Dino上下文的便捷访问方法
 *
 * @author Cody Lu
 */
public final class ContextHelper {

  public static final ContextHelper INST = new ContextHelper();

  private static DinoContext context;

  private static ApplicationContext applicationContext;

  private ContextHelper() {
  }

  /**
   * 设置Spring应用上下文。
   * <p>
   * 用于在应用启动时注入Spring的ApplicationContext实例。
   * </p>
   *
   * @param context Spring应用上下文
   */
  public static void setApplicationContext(ApplicationContext context) {
    applicationContext = context;
  }

  /**
   * 获取Spring应用上下文。
   *
   * @return Spring应用上下文实例
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 设置Dino应用上下文。
   * <p>
   * 用于注入自定义的DinoContext实例，提供租户和用户信息。
   * </p>
   *
   * @param dinoAppContext Dino应用上下文
   */
  public static void setDinoContext(DinoContext dinoAppContext) {
    context = dinoAppContext;
  }

  /**
   * 获取Dino应用上下文。
   *
   * @return Dino应用上下文实例
   */
  public static DinoContext getDinoContext() {
    return context;
  }

  /**
   * 获取当前租户ID。
   * <p>
   * 从当前上下文中获取租户信息，如果租户为null则返回null。
   * </p>
   *
   * @return 当前租户ID，可能为null
   * @throws IllegalArgumentException 如果DinoContext未初始化
   */
  public static String currentTenantId() {
    Assert.notNull(context, "DinoContext bean not found");
    return context.currentTenant() == null ? null : context.currentTenant().getId();
  }

  /**
   * 获取当前租户信息。
   *
   * @return 当前租户对象
   * @throws IllegalArgumentException 如果DinoContext未初始化
   */
  public static Tenant currentTenant() {
    Assert.notNull(context, "DinoContext bean not found");
    return context.currentTenant();
  }

  /**
   * 获取当前用户信息。
   *
   * @param <K> 用户ID的类型
   * @return 当前用户对象
   * @throws IllegalArgumentException 如果DinoContext未初始化
   */
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
