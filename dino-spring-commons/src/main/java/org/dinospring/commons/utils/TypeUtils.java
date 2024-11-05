// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;

import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Cody LU
 * @author JL
 */

@UtilityClass
public class TypeUtils {

  /**
   * 获取某个泛型接口的实际参数
   * @param <T>
   * @param actualType 实际类型
   * @param contextType 需要查询的接口类
   * @param paramIndex 类型参数的索引，从0开始
   * @return
   */
  public static <T> Class<T> getGenericParamClass(Type actualType, Class<?> contextType, int paramIndex) {
    var resolvedType = resolveGenericParamType(ResolvableType.forType(actualType), contextType);
    if (resolvedType == null || !resolvedType.hasGenerics()) {
      return null;
    }
    return cast(resolvedType.getGeneric(paramIndex).resolve());
  }

  /**
   * 获取某个泛型接口的实际类型
   * @param actualType 实际类型
   * @param interfaceType 需要查询的接口类
   * @return
   */
  public static ResolvableType resolveGenericParamType(ResolvableType actualType, Class<?> interfaceType) {
    var actualClass = actualType.resolve();
    if (actualClass == null || !interfaceType.isAssignableFrom(actualType.resolve())) {
      return null;
    }
    if (actualClass.equals(interfaceType)) {
      return actualType;
    }

    ResolvableType superType = actualType.getSuperType();
    if (superType != ResolvableType.NONE) {
      var ret = resolveGenericParamType(superType, interfaceType);
      if (Objects.nonNull(ret)) {
        return cast(ret);
      }
    }
    for (ResolvableType ifc : actualType.getInterfaces()) {
      var ret = resolveGenericParamType(ifc, interfaceType);
      if (Objects.nonNull(ret)) {
        return cast(ret);
      }
    }
    return null;
  }

  /**
   * 获取某个泛型接口的实际参数
   * @param <T>
   * @param inst 对象实例
   * @param contextType 需要查询的接口类
   * @param paramIndex 类型参数的索引，从0开始
   * @return
   */
  public static <T> Class<T> getGenericParamClass(Object inst, Class<?> contextType, int paramIndex) {
    var resolvedType = resolveGenericParamType(ResolvableType.forClass(inst.getClass()), contextType);
    if (resolvedType == null || !resolvedType.hasGenerics()) {
      return null;
    }
    return cast(resolvedType.getGeneric(paramIndex).resolve());
  }

  /**
   * 获取某个父类泛型接口的实际参数
   * @param <T>
   * @param inst 对象实例
   * @param interfaceClass 需要查询的接口类
   * @param paramIndex 类型参数的索引，从0开始
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> getGenericSuperclassParamClass(Object inst, Class<?> interfaceClass, int paramIndex) {
    Type type = inst.getClass().getGenericSuperclass();
    if (type instanceof ParameterizedType tp) {
      if (tp.getRawType().equals(interfaceClass)) {
        var t = tp.getActualTypeArguments()[paramIndex];
        if (t instanceof ParameterizedType) {
          return (Class<T>) ((ParameterizedType) t).getRawType();
        } else {
          return (Class<T>) t;
        }
      }
    }
    return null;
  }

  /**
   * 判断class是否为基本类型或者基本类型的包装类型
   * @param clazz
   * @return
   */
  public static boolean isPrimitiveOrString(@Nonnull Class<?> clazz) {
    return ClassUtils.isPrimitiveOrWrapper(clazz) || clazz == String.class;
  }

  /**
   * 用默认构造函数构建创建新实例
   * @param cls
   * @return
   */
  public static <T> T newInstance(Class<T> cls) {
    try {
      return cls.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  /**
   * 判断instance是否为types中的一种类型
   * @param instance
   * @param types
   * @return
   */
  public static boolean isInstanceOfAny(Object instance, Type... types) {
    for (Type type : types) {
      if (org.apache.commons.lang3.reflect.TypeUtils.isInstance(instance, type)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 类型转换
   * @param object
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T cast(Object object) {
    return (T) object;
  }
}
