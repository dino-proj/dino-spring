// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.annotation;

/**
 * 设置项注解，用于标记配置设置项的元数据
 *
 * @author Cody Lu
 * @since 2021-12-22
 */

public @interface SettingItem {

  /**
   * 设置项所属的组名。
   * <p>
   * 用于对相关的设置项进行分组管理，便于在配置界面中进行组织和展示。
   * </p>
   *
   * @return 设置项组名
   */
  String group();

  /**
   * 设置项的唯一标识名称。
   * <p>
   * 用作设置项的唯一标识符，通常对应配置文件中的键名。
   * </p>
   *
   * @return 设置项名称
   */
  String name();

  /**
   * 设置项的显示标题。
   * <p>
   * 用于在用户界面中显示的友好标题，便于用户理解设置项的含义。
   * </p>
   *
   * @return 设置项标题
   */
  String title();

}