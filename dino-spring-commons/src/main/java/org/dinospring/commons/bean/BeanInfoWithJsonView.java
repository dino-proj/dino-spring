// Copyright 2022 dinospring.cn
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

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.dinospring.commons.function.Suppliers;
import org.dinospring.commons.json.JsonViewUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author tuuboo
 * @date 2022-05-28 03:54:53
 */

public class BeanInfoWithJsonView extends BeanInfo {

  private static final String[] EMPTY_STRING_ARRAY = new String[0];

  private Class<?> activeView;

  private Supplier<String[]> unreadablePropertySupplier = Suppliers
      .lazy(() -> findIgnoredProperties(getActiveView(), true));

  private Supplier<String[]> unwritablePropertySupplier = Suppliers
      .lazy(() -> findIgnoredProperties(getActiveView(), false));

  public BeanInfoWithJsonView(Class<?> beanClass, Class<?> activeView) {
    super(beanClass);
    this.activeView = activeView;
  }

  /**
   * active json view
   * @return the activeView
   */
  public Class<?> getActiveView() {
    return activeView;
  }

  /**
   * unreadable property names
   * @return the unreadable property names, or empty array if not found
   */
  public String[] getUnreadablePropertyNames() {
    return unreadablePropertySupplier.get();
  }

  /**
   * readable property names
   * @return  the readable property names, or empty array if not found
   */
  public String[] getReadablePropertyNames() {
    var ignores = Set.of(getUnreadablePropertyNames());
    var readableProperties = new ArrayList<String>();
    var properties = getPropertyDescriptors();
    for (var property : properties) {
      if (!ignores.contains(property.getName())) {
        readableProperties.add(property.getName());
      }
    }
    return readableProperties.toArray(EMPTY_STRING_ARRAY);
  }

  /**
   * unwritable property names
   * @return the unwritable property names, or empty array if not found
   */
  public String[] getUnwritablePropertyNames() {
    return unwritablePropertySupplier.get();
  }

  /**
   * writable property names
   * @return the writable property names, or empty array if not found
   */
  public String[] getWritablePropertyNames() {
    var ignores = Set.of(getUnwritablePropertyNames());
    var writableProperties = new ArrayList<String>();
    var properties = getPropertyDescriptors();
    for (var property : properties) {
      if (!ignores.contains(property.getName())) {
        writableProperties.add(property.getName());
      }
    }
    return writableProperties.toArray(EMPTY_STRING_ARRAY);
  }

  /**
   * readable property descriptors
   * @return
   */
  public PropertyDescriptor[] getReadablePropertyDescriptors() {
    var ignores = Set.of(getUnreadablePropertyNames());
    var readableProperties = new ArrayList<PropertyDescriptor>();
    var properties = getPropertyDescriptors();
    for (var property : properties) {
      if (!ignores.contains(property.getName())) {
        readableProperties.add(property);
      }
    }
    return readableProperties.toArray(new PropertyDescriptor[0]);
  }

  /**
   * writable property descriptors
   * @return
   */
  public PropertyDescriptor[] getWritablePropertyDescriptors() {
    var ignores = Set.of(getUnwritablePropertyNames());
    var writableProperties = new ArrayList<PropertyDescriptor>();
    var properties = getPropertyDescriptors();
    for (var property : properties) {
      if (!ignores.contains(property.getName())) {
        writableProperties.add(property);
      }
    }
    return writableProperties.toArray(new PropertyDescriptor[0]);
  }

  private String[] findIgnoredProperties(Class<?> activeView, boolean read) {
    var cls = getBeanClass();
    var ignoreProperties = new HashSet<String>();
    var properties = BeanUtils.getPropertyDescriptors(cls);
    //check method
    for (var property : properties) {
      var method = read ? property.getReadMethod() : property.getWriteMethod();
      if (method == null || JsonViewUtils.isInView(method, activeView)) {
        ignoreProperties.add(property.getName());
      }
    }
    //check field
    ReflectionUtils.doWithFields(cls, field -> {
      if (!JsonViewUtils.isInView(field, activeView)) {
        ignoreProperties.add(field.getName());
      }
    });
    if (ignoreProperties.isEmpty()) {
      return EMPTY_STRING_ARRAY;
    }
    return ignoreProperties.toArray(new String[0]);

  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BeanInfoWithJsonView)) {
      return false;
    }
    return super.equals(obj) && ((BeanInfoWithJsonView) obj).activeView.equals(activeView);
  }

  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    hashCode = 29 * hashCode + activeView.hashCode();
    return hashCode;
  }
}
