// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.annotation;

/**
 *
 * @author Cody LU
 * @date 2021-12-22 20:35:55
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