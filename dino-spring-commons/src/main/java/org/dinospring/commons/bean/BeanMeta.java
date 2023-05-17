// Copyright 2022 dinodev.cn
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

package org.dinospring.commons.bean;

/**
 * bean meta info
 * @author tuuboo
 * @date 2022-07-01 10:43:49
 */

public interface BeanMeta {

  /**
   * bean class
   * @return the beanClass
   */
  Class<?> getBeanClass();

  /**
   * bean property names
   * @return the property names
   */
  String[] getPropertyNames();

  /**
   * bean property descriptors of the bean class
   * @return the property descriptors, or empty array if not found
   */
  Property[] getProperties();

  /**
   * bean property descriptor of property name
   * @param propertyName
   * @return the property descriptor, or null if not found
   */
  Property getProperty(String propertyName);

  /**
   * readable property names
   * @return  the readable property names, or empty array if not found
   */
  String[] getReadablePropertyNames();

  /**
   * readable property descriptors
   * @return the readable property descriptors, or empty array if not found
   */
  Property[] getReadableProperties();

  /**
   * writable property names
   * @return the writable property names, or empty array if not found
   */
  String[] getWritablePropertyNames();

  /**
   * writable property descriptors
   * @return the writable property descriptors, or empty array if not found
   */
  Property[] getWritableProperties();

  /**
   * unreadable property names
   * @return the unreadable property names, or empty array if not found
   */
  String[] getUnreadablePropertyNames();

  /**
   * unreadable property descriptors
   * @return the unreadable property descriptors, or empty array if not found
   */
  Property[] getUnreadableProperties();

  /**
   * unwritable property names
   * @return the unwritable property names, or empty array if not found
   */
  String[] getUnwritablePropertyNames();

  /**
   * unwritable property descriptors
   * @return the unwritable property descriptors, or empty array if not found
   */
  Property[] getUnwritableProperties();
}
