// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

/**
 * scope 接口，用于定义scope范围
 * @author Cody Lu
 */
public interface Scope extends Serializable {

  /**
   * scope 的名字
   * @return
   */
  String getName();

  /**
   * scope 顺序
   * @return
   */
  int getOrder();

  /**
   * 比自己优先级低的Scopes
   * @param includeThis 是否包含本Scope
   * @return 比自己小或等于自己排序的Scopes
   */
  Scope[] lowerScopes(boolean includeThis);

  /**
   * 比自己优先级高的Scopes
   * @param includeThis 是否包含本Scope
   * @return 比自己大或等于自己排序的Scopes
   */
  Scope[] higherScopes(boolean includeThis);

  /**
   * 默认的作用域枚举实现。
   * <p>
   * 定义了四个基本的作用域级别，按照优先级从低到高排列：
   * <ul>
   * <li>SYS: 系统级作用域，优先级最高</li>
   * <li>TENANT: 租户级作用域</li>
   * <li>PAGE: 页面级作用域</li>
   * <li>USER: 用户级作用域，优先级最低</li>
   * </ul>
   * </p>
   */
  @AllArgsConstructor
  enum DEFAULT implements Scope {
    //系统级
    SYS("sys", 0),
    //租户级
    TENANT("tenant", 1),
    //页面级
    PAGE("page", 2),
    //用户级
    USER("user", 3);

    private static final Scope[] EMPTY_ARRAY = new Scope[0];

    private String name;
    private int order;

    @Override
    public String getName() {
      return name;
    }

    @Override
    public int getOrder() {
      return order;
    }

    @Override
    public Scope[] lowerScopes(boolean includeThis) {
      return Arrays.stream(values())
          .filter(s -> s.getOrder() < this.getOrder() || (includeThis && s.getOrder() == this.getOrder()))
          .collect(Collectors.toList()).toArray(EMPTY_ARRAY);
    }

    @Override
    public Scope[] higherScopes(boolean includeThis) {
      return Arrays.stream(values())
          .filter(s -> s.getOrder() > this.getOrder() || (includeThis && s.getOrder() == this.getOrder()))
          .collect(Collectors.toList()).toArray(EMPTY_ARRAY);
    }

    @Override
    public String toString() {
      return name;
    }

    /**
     * 获取作用域提供者函数。
     * <p>
     * 返回一个函数，该函数可以根据作用域名称字符串获取对应的Scope实例。
     * </p>
     *
     * @return 作用域提供者函数
     */
    public static Function<String, Scope> provider() {
      return DEFAULT::of;
    }

    /**
     * 根据作用域名称获取对应的Scope实例。
     * <p>
     * 将输入的名称转换为大写后进行匹配。
     * </p>
     *
     * @param name 作用域名称
     * @return 对应的Scope实例
     * @throws IllegalArgumentException 如果找不到对应的作用域
     */
    public static Scope of(String name) {
      return DEFAULT.valueOf(name.toUpperCase());
    }

  }
}
