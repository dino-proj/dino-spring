// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.dinospring.commons.json.JsonViewUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.annotation.Nullable;

/**
 *
 * @author Cody Lu
 * @date 2022-07-01 20:51:22
 */

public final class Property {

  private static Map<Property, Annotation[]> annotationCache = new ConcurrentReferenceHashMap<>();

  private final Class<?> objectType;

  @Nullable
  private final Method readMethod;

  @Nullable
  private final Method writeMethod;

  @Nullable
  private final Field field;

  private final String name;

  private final MethodParameter methodParameter;

  @Nullable
  private Annotation[] annotations;

  public Property(Class<?> objectType, @Nullable Method readMethod, @Nullable Method writeMethod) {
    this(objectType, readMethod, writeMethod, null);
  }

  public Property(
      Class<?> objectType, @Nullable Method readMethod, @Nullable Method writeMethod, @Nullable String name) {

    this.objectType = objectType;
    this.readMethod = readMethod;
    this.writeMethod = writeMethod;
    this.methodParameter = this.resolveMethodParameter();
    this.name = (name != null ? name : this.resolveName());
    this.field = this.resolveField();
  }

  /**
   * The object declaring this property, either directly or in a superclass the object extends.
   * @return the object type
   */
  public Class<?> getObjectType() {
    return this.objectType;
  }

  /**
   * The name of the property: e.g. 'foo'
   * @return the name of the property
   */
  public String getName() {
    return this.name;
  }

  /**
   * The property type: e.g. {@code java.lang.String}
   * @return the property type
   */
  public Class<?> getType() {
    return this.methodParameter.getParameterType();
  }

  /**
   * The ResolvableType property type.
   * @return the ResolvableType property type
   */
  public ResolvableType getResolvableType() {
    if (this.methodParameter != null) {
      return ResolvableType.forMethodParameter(this.methodParameter);
    } else if (this.field != null) {
      return ResolvableType.forField(this.field);
    } else {
      throw new IllegalStateException("No method or field found for property: " + this.name);
    }
  }

  /**
   * The property getter method: e.g. {@code getFoo()}
   * @return the property getter method, or null if not found
   */
  @Nullable
  public Method getReadMethod() {
    return this.readMethod;
  }

  /**
   * The property setter method: e.g. {@code setFoo(String)}
   * @return the property setter method, or null if not found
   */
  @Nullable
  public Method getWriteMethod() {
    return this.writeMethod;
  }

  /**
   * The property Field: e.g. {@code String foo;}
   * @return the property Field, or null if not found
   */
  @Nullable
  public Field getField() {
    return this.field;
  }

  /**
   * The property is readable.
   * @return true if the property is readable
   */
  public boolean isReadable() {
    return Objects.nonNull(this.readMethod);
  }

  /**
   * The property is writable.
   * @return true if the property is writable
   */
  public boolean isWritable() {
    return Objects.nonNull(this.writeMethod);
  }

  /**
   * The property is readable or writable in given view.
   * @param activeView active jsonView
   * @return true if the property is readable or writable in given view
   */
  public boolean isVisiableInJsonView(@Nullable Class<?> activeView) {
    if (Objects.isNull(activeView) || Objects.isNull(this.annotations)) {
      return true;
    }
    for (Annotation annotation : this.annotations) {
      if (annotation.getClass().equals(JsonView.class)) {
        JsonView jsonView = (JsonView) annotation;
        if (!JsonViewUtils.isInView(activeView, jsonView.value())) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * is the property readable && visible in the given view.
   * @param activeView active jsonView
   * @return true if the property is readable and visible in the given view.
   */
  public boolean isReadableInJsonView(@Nullable Class<?> activeView) {
    if (Objects.isNull(this.readMethod)) {
      return false;
    }
    if (Objects.isNull(activeView)) {
      return true;
    }

    var jsonView = this.getReadAnnotation(JsonView.class);
    if (Objects.nonNull(jsonView) && !JsonViewUtils.isInView(activeView, jsonView.value())) {
      return false;
    }
    return true;
  }

  /**
   * is the property writable && visible in the given view.
   * @param activeView active jsonView
   * @return true if the property is writable and visible in the given view.
   */
  public boolean isWritableInJsonView(@Nullable Class<?> activeView) {
    if (Objects.isNull(this.writeMethod)) {
      return false;
    }
    if (Objects.isNull(activeView)) {
      return true;
    }

    var jsonView = this.getWriteAnnotation(JsonView.class);
    if (Objects.nonNull(jsonView) && !JsonViewUtils.isInView(activeView, jsonView.value())) {
      return false;
    }
    return true;
  }

  /**
   * The method parameter for the property getter or setter method: e.g. {@code MethodParameter(getFoo())}
   * @return the method parameter for the property getter or setter method
   */
  public MethodParameter getMethodParameter() {
    return this.methodParameter;
  }

  /**
   * The property Annotations
   * @return the property Annotations on the getter/setter method or field
   */
  public Annotation[] getAnnotations() {
    if (this.annotations == null) {
      this.annotations = this.resolveAnnotations();
    }
    return this.annotations;
  }

  /**
   * The property Annotation on getter method
   * @param annotationType the annotation type
   * @return the property Annotation on getter method or field
   */
  public <A extends Annotation> A getReadAnnotation(Class<A> annotationType) {
    var annon = AnnotationUtils.getAnnotation(this.readMethod, annotationType);
    if (Objects.isNull(annon) && Objects.nonNull(this.field)) {
      annon = AnnotationUtils.getAnnotation(this.field, annotationType);
    }
    return annon;
  }

  /**
   * The property Annotation on setter method
   * @param annotationType the annotation type
   * @return the property Annotation on setter method or field
   */
  public <A extends Annotation> A getWriteAnnotation(Class<A> annotationType) {
    var annon = AnnotationUtils.getAnnotation(this.writeMethod, annotationType);
    if (Objects.isNull(annon) && Objects.nonNull(this.field)) {
      annon = AnnotationUtils.getAnnotation(this.field, annotationType);
    }
    return annon;
  }

  // Internal helpers

  private String resolveName() {
    if (Objects.nonNull(this.readMethod)) {
      int index = this.readMethod.getName().indexOf("get");
      if (index != -1) {
        index += 3;
      } else {
        index = this.readMethod.getName().indexOf("is");
        if (index != -1) {
          index += 2;
        } else {
          // Record-style plain accessor method, e.g. name()
          index = 0;
        }
      }
      return StringUtils.uncapitalize(this.readMethod.getName().substring(index));
    } else if (Objects.nonNull(this.writeMethod)) {
      int index = this.writeMethod.getName().indexOf("set");
      if (index == -1) {
        throw new IllegalArgumentException("Not a setter method");
      }
      index += 3;
      return StringUtils.uncapitalize(this.writeMethod.getName().substring(index));
    } else {
      throw new IllegalStateException("Property is neither readable nor writeable");
    }
  }

  private MethodParameter resolveMethodParameter() {
    MethodParameter read = this.resolveReadMethodParameter();
    MethodParameter write = this.resolveWriteMethodParameter();
    if (write == null) {
      if (read == null) {
        throw new IllegalStateException("Property is neither readable nor writeable");
      }
      return read;
    }
    if (read != null) {
      Class<?> readType = read.getParameterType();
      Class<?> writeType = write.getParameterType();
      if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
        return read;
      }
    }
    return write;
  }

  @Nullable
  private MethodParameter resolveReadMethodParameter() {
    if (this.getReadMethod() == null) {
      return null;
    }
    return new MethodParameter(this.getReadMethod(), -1).withContainingClass(this.getObjectType());
  }

  @Nullable
  private MethodParameter resolveWriteMethodParameter() {
    if (this.getWriteMethod() == null) {
      return null;
    }
    return new MethodParameter(this.getWriteMethod(), 0).withContainingClass(this.getObjectType());
  }

  private Annotation[] resolveAnnotations() {
    Annotation[] annotations = annotationCache.get(this);
    if (annotations == null) {
      Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<>();
      this.addAnnotationsToMap(annotationMap, this.getReadMethod());
      this.addAnnotationsToMap(annotationMap, this.getWriteMethod());
      this.addAnnotationsToMap(annotationMap, this.getField());
      annotations = annotationMap.values().toArray(new Annotation[0]);
      annotationCache.put(this, annotations);
    }
    return annotations;
  }

  private void addAnnotationsToMap(
      Map<Class<? extends Annotation>, Annotation> annotationMap, @Nullable AnnotatedElement object) {

    if (object != null) {
      for (Annotation annotation : object.getAnnotations()) {
        annotationMap.put(annotation.annotationType(), annotation);
      }
    }
  }

  @Nullable
  private Class<?> declaringClass() {
    if (this.getReadMethod() != null) {
      return this.getReadMethod().getDeclaringClass();
    } else if (this.getWriteMethod() != null) {
      return this.getWriteMethod().getDeclaringClass();
    } else {
      return null;
    }
  }

  @Nullable
  private Field resolveField() {
    String name = this.getName();
    if (!StringUtils.hasLength(name)) {
      return null;
    }
    Field field = null;
    Class<?> declaringClass = this.declaringClass();
    if (declaringClass != null) {
      field = ReflectionUtils.findField(declaringClass, name);
      if (field == null) {
        // Same lenient fallback checking as in CachedIntrospectionResults...
        field = ReflectionUtils.findField(declaringClass, StringUtils.uncapitalize(name));
        if (field == null) {
          field = ReflectionUtils.findField(declaringClass, StringUtils.capitalize(name));
        }
      }
    }
    return field;
  }

  @Override
  public boolean equals(@Nullable Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Property otherProperty)) {
      return false;
    }
    return (ObjectUtils.nullSafeEquals(this.objectType, otherProperty.objectType) &&
        ObjectUtils.nullSafeEquals(this.name, otherProperty.name) &&
        ObjectUtils.nullSafeEquals(this.readMethod, otherProperty.readMethod) &&
        ObjectUtils.nullSafeEquals(this.writeMethod, otherProperty.writeMethod));
  }

  @Override
  public int hashCode() {
    return (ObjectUtils.nullSafeHashCode(this.objectType) * 31 + ObjectUtils.nullSafeHashCode(this.name));
  }

}
