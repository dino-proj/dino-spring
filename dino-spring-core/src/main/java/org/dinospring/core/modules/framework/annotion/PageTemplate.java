package org.dinospring.core.modules.framework.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dinospring.core.modules.framework.PageType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageTemplate {

  String name();

  String title();

  PageType type();

  String icon() default "";

  String appPath() default "";

  String pcPath() default "";

  String description() default "";
}
