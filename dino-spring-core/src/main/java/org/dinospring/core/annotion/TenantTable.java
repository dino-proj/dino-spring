package org.dinospring.core.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TenantTable {
  /**
   * 表名和租户ID之间的分隔符，默认"_"
   */
  String sep() default "_";

  TenantLevel level() default TenantLevel.TABLE;

  enum TenantLevel {
    TABLE, SCHEMA, ROW
  }
}
