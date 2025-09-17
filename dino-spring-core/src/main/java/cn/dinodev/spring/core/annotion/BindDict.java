// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lombok.RequiredArgsConstructor;

/**
 * 绑定字典注解
 * @author Cody Lu
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BindDict {

  /***
   * 绑定数据字典Key
   * @return
   */
  String key();

  /***
   * 数据字典项取值字段
   * @return
   */
  DictFilds field() default DictFilds.VALUE;

  /**
   * 数据字典KEY的优先查询值，默认是租户级别
   * @return
   */
  Scope scope() default Scope.TENANT;

  @RequiredArgsConstructor
  public enum DictFilds {
    //item_code
    NAME("item_code"),
    //item_label
    VALUE("item_label"),
    //item_icon
    ICON("item_icon");

    private final String fieldName;

    @Override
    public String toString() {
      return this.fieldName;
    }
  }

  public enum Scope {
    //系统级字典
    SYSTEM,
    //租户级字典
    TENANT,
    //租户优先，租户未找到，则使用系统级别
    TENENT_PREFER
  }

}
