// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.bean;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.springframework.beans.BeanUtils;

import cn.dinodev.spring.commons.function.Suppliers;

/**
 * Bean元信息实现类，提供Bean元数据的具体实现
 *
 * @author Cody Lu
 * @since 2022-05-30
 */

public class BeanMetaImpl implements BeanMeta {
  private final Class<?> beanClass;

  private final Supplier<Map<String, Property>> propertyDescriptorsSupplier;

  /**
   * 构造一个BeanMetaImpl实例。
   * <p>
   * 根据指定的Bean类创建元信息对象，用于后续的属性访问和操作。
   * </p>
   *
   * @param beanClass Bean的Class对象
   */
  public BeanMetaImpl(Class<?> beanClass) {
    this.beanClass = beanClass;
    this.propertyDescriptorsSupplier = Suppliers.lazy(() -> {
      PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(this.beanClass);
      Map<String, Property> map = new ConcurrentHashMap<>(pds.length);
      for (PropertyDescriptor propertyDescriptor : pds) {
        map.put(propertyDescriptor.getName(), new Property(this.beanClass, propertyDescriptor.getReadMethod(),
            propertyDescriptor.getWriteMethod(), propertyDescriptor.getName()));
      }
      return map;
    });
  }

  @Override
  public Class<?> getBeanClass() {
    return beanClass;
  }

  @Override
  public Property getProperty(String propertyName) {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.get(propertyName);
  }

  @Override
  public Property[] getProperties() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().toArray(new Property[0]);
  }

  @Override
  public String[] getPropertyNames() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public String[] getReadablePropertyNames() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.nonNull(p.getReadMethod())).map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public Property[] getReadableProperties() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.nonNull(p.getReadMethod()))
        .toArray(Property[]::new);
  }

  @Override
  public String[] getWritablePropertyNames() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.nonNull(p.getWriteMethod())).map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public Property[] getWritableProperties() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.nonNull(p.getWriteMethod()))
        .toArray(Property[]::new);
  }

  @Override
  public String[] getUnreadablePropertyNames() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.isNull(p.getReadMethod()))
        .map(Property::getName).toArray(String[]::new);
  }

  @Override
  public Property[] getUnreadableProperties() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.isNull(p.getReadMethod()))
        .toArray(Property[]::new);
  }

  @Override
  public String[] getUnwritablePropertyNames() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.isNull(p.getWriteMethod()))
        .map(Property::getName).toArray(String[]::new);
  }

  @Override
  public Property[] getUnwritableProperties() {
    Map<String, Property> pds = propertyDescriptorsSupplier.get();
    return pds.values().stream().filter(p -> Objects.isNull(p.getWriteMethod()))
        .toArray(Property[]::new);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BeanMetaImpl && ((BeanMetaImpl) obj).beanClass.equals(beanClass);
  }

  @Override
  public int hashCode() {
    return getBeanClass().hashCode();
  }

}
