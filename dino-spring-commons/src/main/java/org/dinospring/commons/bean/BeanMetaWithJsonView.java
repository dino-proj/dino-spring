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

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author tuuboo
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
