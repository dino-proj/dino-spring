// Copyright 2023 dinodev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.jdbc;

import java.io.Serializable;
import java.util.Set;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.lang.NonNull;

import jakarta.persistence.Table;

/**
 *
 * @author Cody Lu
 * @date 2023-05-18 00:23:48
 */

public class DinoJdbcSimpleTypeHolder extends SimpleTypeHolder {

  public DinoJdbcSimpleTypeHolder(SimpleTypeHolder source) {
    super(Set.of(Serializable.class), source);
  }

  @Override
  public boolean isSimpleType(@NonNull Class<?> type) {
    var isSimple = super.isSimpleType(type);
    if (isSimple) {
      return AnnotationUtils.findAnnotation(type, Table.class) == null;
    } else {
      return false;
    }
  }
}
