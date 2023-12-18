// Copyright 2023 dinospring.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.autoconfig;

import java.util.Collections;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.model.SimpleTypeHolder;

import jakarta.persistence.Table;

/**
 *
 * @author Cody Lu
 * @date 2023-05-18 00:23:48
 */

public class DinoSimpleTypeHolder extends SimpleTypeHolder {

  public DinoSimpleTypeHolder(SimpleTypeHolder source) {
    super(Collections.emptySet(), source);
  }

  @Override
  public boolean isSimpleType(Class<?> type) {
    var isSimple = super.isSimpleType(type);
    if (isSimple) {
      return AnnotationUtils.findAnnotation(type, Table.class) == null;
    } else {
      return false;
    }
  }
}
