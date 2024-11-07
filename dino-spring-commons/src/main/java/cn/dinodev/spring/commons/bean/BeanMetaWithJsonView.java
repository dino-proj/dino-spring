// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.bean;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Cody Lu
 * @date 2022-05-28 03:54:53
 */

public class BeanMetaWithJsonView implements BeanMeta {

  private final BeanMeta beanMeta;
  private final Class<?> activeView;

  public BeanMetaWithJsonView(BeanMeta beanMeta, Class<?> activeView) {
    this.beanMeta = beanMeta;
    this.activeView = activeView;
  }

  /**
   * active json view
   * @return the activeView
   */
  public Class<?> getActiveView() {
    return activeView;
  }

  @Override
  public Class<?> getBeanClass() {
    return beanMeta.getBeanClass();
  }

  @Override
  public String[] getPropertyNames() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> p.isVisiableInJsonView(this.activeView)).map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public Property[] getProperties() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> p.isVisiableInJsonView(this.activeView)).toArray(Property[]::new);
  }

  @Override
  public Property getProperty(String propertyName) {
    var pd = beanMeta.getProperty(propertyName);
    if (Objects.nonNull(pd) && pd.isVisiableInJsonView(this.activeView)) {
      return pd;
    }
    return null;
  }

  @Override
  public Property[] getReadableProperties() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> p.isReadableInJsonView(this.activeView)).toArray(Property[]::new);
  }

  @Override
  public Property[] getWritableProperties() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> p.isWritableInJsonView(this.activeView)).toArray(Property[]::new);
  }

  @Override
  public String[] getReadablePropertyNames() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> p.isReadableInJsonView(this.activeView)).map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public String[] getWritablePropertyNames() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> p.isWritableInJsonView(this.activeView)).map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public String[] getUnreadablePropertyNames() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> !p.isReadableInJsonView(this.activeView)).map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public Property[] getUnreadableProperties() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> !p.isReadableInJsonView(this.activeView)).toArray(Property[]::new);
  }

  @Override
  public String[] getUnwritablePropertyNames() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> !p.isWritableInJsonView(this.activeView)).map(Property::getName)
        .toArray(String[]::new);
  }

  @Override
  public Property[] getUnwritableProperties() {
    var pds = beanMeta.getProperties();
    return Arrays.stream(pds).filter(p -> !p.isWritableInJsonView(this.activeView)).toArray(Property[]::new);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BeanMetaWithJsonView)) {
      return false;
    }
    return beanMeta.equals(((BeanMetaWithJsonView) obj).beanMeta)
        && activeView.equals(((BeanMetaWithJsonView) obj).activeView);
  }

  @Override
  public int hashCode() {
    int hashCode = beanMeta.hashCode();
    hashCode = 29 * hashCode + activeView.hashCode();
    return hashCode;
  }

}
