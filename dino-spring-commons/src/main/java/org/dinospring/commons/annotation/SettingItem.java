// Copyright 2021 dinodev.cn
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