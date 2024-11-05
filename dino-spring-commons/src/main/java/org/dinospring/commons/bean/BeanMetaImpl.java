// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

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
 * @author Cody LU
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
