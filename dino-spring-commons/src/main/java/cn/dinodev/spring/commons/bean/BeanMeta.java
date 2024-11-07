// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.bean;

/**
 * bean meta info
 * @author Cody Lu
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
