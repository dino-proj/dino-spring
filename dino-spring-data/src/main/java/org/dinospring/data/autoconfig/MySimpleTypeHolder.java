package org.dinospring.data.autoconfig;

import java.util.Collections;

import javax.persistence.Table;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.model.SimpleTypeHolder;

public class MySimpleTypeHolder extends SimpleTypeHolder {

  public MySimpleTypeHolder(SimpleTypeHolder source) {
    super(Collections.emptySet(), source);
  }

  @Override
  public boolean isSimpleType(Class<?> type) {
    var isSimple = super.isSimpleType(type);
    if (!isSimple) {
      return AnnotationUtils.findAnnotation(type, Table.class) == null;
    } else {
      return true;
    }
  }
}
