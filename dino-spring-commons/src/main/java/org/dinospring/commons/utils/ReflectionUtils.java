// Copyright 2021 dinospring.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
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
   * ???????????????
   *
   * @param entity    ??????
   * @param fieldName ????????????
   * @return ?????????
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
   * ????????????????????????
   * </p>
   *
   * @param clazz ??????
   * @param index ??????????????????
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
   * ?????????????????????????????????
   * </p>
   *
   * @param clazz ?????????
   */
  public static Map<String, Field> getFieldMap(Class<?> clazz) {
    List<Field> fieldList = getFieldList(clazz);
    return CollectionUtils.isNotEmpty(fieldList)
        ? fieldList.stream().collect(Collectors.toMap(Field::getName, field -> field))
        : Collections.emptyMap();
  }

  /**
   * <p>
   * ?????????????????????????????????
   * </p>
   *
   * @param clazz ?????????
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
      /* ?????????????????? */
      Map<String, Field> fieldMap = excludeOverrideSuperField(fields, superFields);
      /*
       * ????????????????????????????????????????????????????????????????????????
       * ???????????????????????????????????????????????????????????? createTime ????????????
       * ????????????????????????????????? ` private transient Date createTime; `
       */
      return fieldMap.values().stream()
          /* ?????????????????? */
          .filter(f -> !Modifier.isStatic(f.getModifiers()))
          /* ?????? transient???????????????????????? */
          .filter(f -> !Modifier.isTransient(f.getModifiers())).collect(Collectors.toList());
    });
  }

  /**
   * <p>
   * ????????????????????????
   * </p>
   *
   * @param fields         ????????????
   * @param superFieldList ????????????
   */
  public static Map<String, Field> excludeOverrideSuperField(Field[] fields, List<Field> superFieldList) {
    // ????????????
    Map<String, Field> fieldMap = Stream.of(fields).collect(toMap(Field::getName, identity(), (u, v) -> {
      throw new IllegalStateException(String.format("Duplicate key %s", u));
    }, LinkedHashMap::new));
    superFieldList.stream().filter(field -> !fieldMap.containsKey(field.getName()))
        .forEach(f -> fieldMap.put(f.getName(), f));
    return fieldMap;
  }

  /**
   * ????????????????????????????????????????????????
   *
   * @param clazz class
   * @return ???????????????????????????????????????
   */
  public static boolean isPrimitiveOrWrapper(@Nonnull Class<?> clazz) {
    return (clazz.isPrimitive() || PRIMITIVE_WRAPPER_TYPE_MAP.containsKey(clazz));
  }
}
