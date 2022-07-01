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
import java.util.Objects;
import java.util.function.Supplier;

import org.dinospring.commons.function.Suppliers;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author tuuboo
 * @date 2022-05-30 10:31:18
 */

public class BeanMetaImpl implements BeanMeta {
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

  public BeanMetaImpl(Class<?> beanClass) {
    this.beanClass = beanClass;
  }

  @Override
  public Class<?> getBeanClass() {
    return beanClass;
  }

  @Override
  public Property getProperty(String propertyName) {
    var pds = propertyDescriptorsSupplier.get();
    return pds.get(propertyName);
  }

  @Override
  public Property[] getProperties() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().toArray(new Property[pds.size()]);
  }

  @Override
  public String[] getPropertyNames() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().map(p -> p.getName())
        .toArray(String[]::new);
  }

  @Override
  public String[] getReadablePropertyNames() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.nonNull(p.getReadMethod())).map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public Property[] getReadableProperties() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.nonNull(p.getReadMethod()))
        .toArray(Property[]::new);
  }

  @Override
  public String[] getWritablePropertyNames() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.nonNull(p.getWriteMethod())).map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public Property[] getWritableProperties() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.nonNull(p.getWriteMethod()))
        .toArray(Property[]::new);
  }

  @Override
  public String[] getUnreadablePropertyNames() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.isNull(p.getReadMethod()))
        .map(Property::getName).toArray(String[]::new);
  }

  @Override
  public Property[] getUnreadableProperties() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.isNull(p.getReadMethod()))
        .toArray(Property[]::new);
  }

  @Override
  public String[] getUnwritablePropertyNames() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.isNull(p.getWriteMethod()))
        .map(Property::getName).toArray(String[]::new);
  }

  @Override
  public Property[] getUnwritableProperties() {
    var pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.isNull(p.getWriteMethod()))
        .toArray(Property[]::new);
  }

  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof BeanInfo)) {
      return false;
    }
    return ((BeanMetaImpl) obj).beanClass.equals(beanClass);
  }

  @Override
  public int hashCode() {
    return getBeanClass().hashCode();
  }

}
