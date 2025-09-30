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

  String group();

  String name();

  String title();

}
/** type Configuration implements DescribedEntity {
  id: ID!
  name: String!
  description: String
  link: String
  icon: String
  plugin: Plugin
  tabs: [ConfigurationTab]!
}
DescribedEntity {
  name: String
  description: String
  link: String
}

  return {
    value: JSON.stringify(getTransformedValue(prompt, data.value)),
    name: data.name,
    checked: data.checked,
    disabled: data.disabled,
    isDefault: data.value === defaultValue
  }
  **/