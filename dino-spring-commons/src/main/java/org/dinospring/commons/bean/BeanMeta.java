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

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.dinospring.commons.function.Suppliers;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.Property;

/**
 *
 * @author tuuboo
 * @date 2022-05-30 10:31:18
 */

public class BeanMeta {
  private Class<?> beanClass;

  private Supplier<Map<String, Property>> propertyDescriptorsSupplier = Suppliers.lazy(() -> {
    var pds = BeanUtils.getPropertyDescriptors(beanClass);
    Map<String, Property> map = new LinkedHashMap<>(pds.length);
    for (PropertyDescriptor propertyDescriptor : pds) {
      map.put(propertyDescriptor.getName(), new Property(beanClass, propertyDescriptor.getReadMethod(),
          propertyDescriptor.getWriteMethod(), propertyDescriptor.getName()));
    }
    return map;
  });

  public BeanMeta(Class<?> beanClass) {
    this.beanClass = beanClass;
  }

  /**
   * bean class
   * @return the beanClass
   */
  public Class<?> getBeanClass() {
    return beanClass;
  }

  /**
   * bean property descriptor of property name
   * @param propertyName
   * @return the property descriptor, or null if not found
   */
  public Property getPropertyDescriptor(String propertyName) {
    var pds = propertyDescriptorsSupplier.get();
    return pds.get(propertyName);
  }

  /**
   * bean property descriptors of the bean class
   * @return the property descriptors, or empty array if not found
   */
  public Property[] getPropertyDescriptors() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().toArray(new Property[pds.size()]);
  }

  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof BeanInfo)) {
      return false;
    }
    return ((BeanMeta) obj).beanClass.equals(beanClass);
  }

  @Override
  public int hashCode() {
    return getBeanClass().hashCode();
  }
}
