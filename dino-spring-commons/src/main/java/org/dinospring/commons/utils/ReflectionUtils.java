// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.utils;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 * @date 2022-03-07 19:18:07
 */

@UtilityClass
@Slf4j
public class ReflectionUtils {

  /**
   * class field cache
   */
  private static final Map<Class<?>, List<Field>> CLASS_FIELD_CACHE = new ConcurrentHashMap<>();

  private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP = new IdentityHashMap<>(8);

  static {
    PRIMITIVE_WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
    PRIMITIVE_WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
    PRIMITIVE_WRAPPER_TYPE_MAP.put(Character.class, char.class);
    PRIMITIVE_WRAPPER_TYPE_MAP.put(Double.class, double.class);
    PRIMITIVE_WRAPPER_TYPE_MAP.put(Float.class, float.class);
    PRIMITIVE_WRAPPER_TYPE_MAP.put(Integer.class, int.class);
    PRIMITIVE_WRAPPER_TYPE_MAP.put(Long.class, long.class);
    PRIMITIVE_WRAPPER_TYPE_MAP.put(Short.class, short.class);
  }

  /**
   * 获取字段值
   *
   * @param entity    实体
   * @param fieldName 字段名称
   * @return 属性值
   * @throws IllegalAccessException
   */
  public static Object getFieldValue(Object entity, String fieldName) throws IllegalAccessException {
    Class<?> cls = entity.getClass();
    Map<String, Field> fieldMaps = getFieldMap(cls);
    try {
      @Nonnull
      var field = fieldMaps.get(fieldName);
      return field.get(entity);
    } catch (ReflectiveOperationException e) {
      log.error("Error: Cannot read field in {}. ", cls.getSimpleName(), e);
      throw new IllegalAccessException(e.getMessage());
    }
  }

  /**
   * <p>
   * 反射对象获取泛型
   * </p>
   *
   * @param clazz 对象
   * @param index 泛型所在位置
   * @return Class
   */
  public static Class<?> getSuperClassGenericType(final Class<?> clazz, final int index) {
    var genType = clazz.getGenericSuperclass();
    if (!(genType instanceof ParameterizedType)) {
      log.warn("Warn: {}'s superclass not ParameterizedType", clazz.getSimpleName());
      return Object.class;
    }
    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
    if (index >= params.length || index < 0) {
      log.warn("Warn: Index: {}, Size of %s's Parameterized Type: {} .", index, clazz.getSimpleName(), params.length);
      return Object.class;
    }
    if (!(params[index] instanceof Class)) {
      log.warn("Warn: {} not set the actual class on superclass generic parameter", clazz.getSimpleName());
      return Object.class;
    }
    return (Class<?>) params[index];
  }

  /**
   * <p>
   * 获取该类的所有属性列表
   * </p>
   *
   * @param clazz 反射类
   */
  public static Map<String, Field> getFieldMap(Class<?> clazz) {
    List<Field> fieldList = getFieldList(clazz);
    return CollectionUtils.isNotEmpty(fieldList)
        ? fieldList.stream().collect(Collectors.toMap(Field::getName, field -> field))
        : Collections.emptyMap();
  }

  /**
   * <p>
   * 获取该类的所有属性列表
   * </p>
   *
   * @param clazz 反射类
   */
  public static List<Field> getFieldList(Class<?> clazz) {
    if (Objects.isNull(clazz)) {
      return Collections.emptyList();
    }
    return CLASS_FIELD_CACHE.computeIfAbsent(clazz, k -> {
      Field[] fields = k.getDeclaredFields();
      List<Field> superFields = new ArrayList<>();
      Class<?> currentClass = k.getSuperclass();
      while (currentClass != null) {
        Field[] declaredFields = currentClass.getDeclaredFields();
        Collections.addAll(superFields, declaredFields);
        currentClass = currentClass.getSuperclass();
      }
      /* 排除重载属性 */
      Map<String, Field> fieldMap = excludeOverrideSuperField(fields, superFields);
      /*
       * 父类属性过滤后处理忽略部分，支持过滤父类属性功能
       * 场景：中间表不需要记录创建时间，忽略父类 createTime 公共属性
       * 中间表实体重写父类属性 ` private transient Date createTime; `
       */
      return fieldMap.values().stream()
          /* 过滤静态属性 */
          .filter(f -> !Modifier.isStatic(f.getModifiers()))
          /* 过滤 transient关键字修饰的属性 */
          .filter(f -> !Modifier.isTransient(f.getModifiers())).collect(Collectors.toList());
    });
  }

  /**
   * <p>
   * 排序重置父类属性
   * </p>
   *
   * @param fields         子类属性
   * @param superFieldList 父类属性
   */
  public static Map<String, Field> excludeOverrideSuperField(Field[] fields, List<Field> superFieldList) {
    // 子类属性
    Map<String, Field> fieldMap = Stream.of(fields).collect(toMap(Field::getName, identity(), (u, v) -> {
      throw new IllegalStateException(String.format("Duplicate key %s", u));
    }, LinkedHashMap::new));
    superFieldList.stream().filter(field -> !fieldMap.containsKey(field.getName()))
        .forEach(f -> fieldMap.put(f.getName(), f));
    return fieldMap;
  }

  /**
   * 判断是否为基本类型或基本包装类型
   *
   * @param clazz class
   * @return 是否基本类型或基本包装类型
   */
  public static boolean isPrimitiveOrWrapper(@Nonnull Class<?> clazz) {
    return (clazz.isPrimitive() || PRIMITIVE_WRAPPER_TYPE_MAP.containsKey(clazz));
  }
}
