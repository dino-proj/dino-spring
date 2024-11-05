// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.json.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import org.dinospring.commons.json.AnnotionedJsonTypeIdResolver;
import org.springframework.core.annotation.AliasFor;

/**
 * json类型多态的实现，请使用<pre>@ {@link com.fasterxml.jackson.annotation.JsonTypeName} </pre>来定义实现类的ID
 * 如果从自定义注解中抽取id，请请先注册，例如：
 * 将<pre> @PageTemplate </pre>注解中的name作为ID，这按照如下方式添加：
 *  <pre> AnnotionedJsonTypeIdResolver.addAnnotion(PageTemplate.class, PageTemplate::name, "com.botbrain");</pre>
 *
 * @author Cody LU
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@JsonTypeInfo(use = Id.CUSTOM, include = As.PROPERTY, visible = true, property = "@t")
@JsonTypeIdResolver(AnnotionedJsonTypeIdResolver.class)
@JacksonAnnotation
public @interface JsonDiscriminator {

  /**
   * Alias for {@link JsonTypeInfo#property()}.
   *
   * @return
   */
  @AliasFor(annotation = JsonTypeInfo.class)
  String property() default "@t";
}
