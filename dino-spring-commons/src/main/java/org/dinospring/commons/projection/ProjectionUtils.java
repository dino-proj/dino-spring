// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.projection;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.commons.bean.BeanMetaUtils;
import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.function.Suppliers;
import org.dinospring.commons.utils.TypeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.ResolvableType;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Cody Lu
 * @date 2022-06-03 22:39:47
 */

@UtilityClass
public class ProjectionUtils {
  private static final Supplier<ProjectionFactory> PROJECTION_FACTORY_SUPPLIER = Suppliers.lazy(() -> {
    return ContextHelper.findBean(ProjectionFactory.class);
  });

  /**
   * 获取 ProjectionFactory bean 实例
   * @return
   */
  public static ProjectionFactory resolveProjectionFactory() {
    return PROJECTION_FACTORY_SUPPLIER.get();
  }

  /**
   * Copy the property values of the given source bean into the target bean.
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * @param source the source bean
   * @param target the target bean
   * @throws BeansException if the projecting failed
   * @see BeanWrapper
   */
  public static void projectProperties(Object source, Object target) throws BeansException {
    projectProperties(source, target, null, Collections.emptySet());
  }

  /**
   * Copy the property values of the given source bean into the given target bean,
   * only setting properties defined in the given "editable" class (or interface).
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * @param source the source bean
   * @param target the target bean
   * @param editable the class (or interface) to restrict property setting to
   * @throws BeansException if the projecting failed
   * @see BeanWrapper
   */
  public static void projectProperties(Object source, Object target, Class<?> editable) throws BeansException {
    projectProperties(source, target, editable, Collections.emptySet());
  }

  /**
   * project the property values of the given source bean into the given target bean,
   * ignoring the given "ignoreProperties".
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * <p>This is just a convenience method. For more complex transfer needs,
   * consider using a full BeanWrapper.
   * @param source the source bean
   * @param target the target bean
   * @param ignoreProperties array of property names to ignore
   * @throws BeansException if the projecting failed
   * @see BeanWrapper
   */
  public static void projectProperties(Object source, Object target, String... ignoreProperties) throws BeansException {
    Set<String> ignoreList = (ignoreProperties != null && ignoreProperties.length > 0 ? Set.of(ignoreProperties)
        : Collections.emptySet());
    projectProperties(source, target, null, ignoreList);
  }

  private static void projectProperties(Object source, Object target, @Nullable Class<?> editable,
      Set<String> ignoreProperties) throws BeansException {

    Assert.notNull(source, "Source must not be null");
    Assert.notNull(target, "Target must not be null");

    Class<?> actualEditable = target.getClass();
    if (editable != null) {
      if (!editable.isInstance(target)) {
        throw new IllegalArgumentException("Target class [" + target.getClass().getName()
            + "] not assignable to Editable class [" + editable.getName() + "]");
      }
      actualEditable = editable;
    }
    PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);

    for (PropertyDescriptor targetPd : targetPds) {
      var writeMethod = targetPd.getWriteMethod();
      var sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
      if (writeMethod == null || ignoreProperties.contains(targetPd.getName()) || sourcePd == null
          || sourcePd.getReadMethod() == null) {
        continue;
      }

      Method readMethod = sourcePd.getReadMethod();

      try {
        projectProperty(source, target, readMethod, writeMethod);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        throw new FatalBeanException("Could not project property '" + targetPd.getName() + "' from source to target",
            ex);
      }
    }
  }

  private void projectProperty(Object source, Object target, Method readMethod, Method writeMethod)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    ResolvableType sourceResolvableType = ResolvableType.forMethodReturnType(readMethod);
    ResolvableType targetResolvableType = ResolvableType.forMethodParameter(writeMethod, 0);

    // Ignore generic types in assignable check if either ResolvableType has unresolvable generics.
    boolean isAssignable = (sourceResolvableType.hasUnresolvableGenerics()
        || targetResolvableType.hasUnresolvableGenerics()
            ? ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())
            : (targetResolvableType.isAssignableFrom(sourceResolvableType)
                || BeanUtils.isSimpleValueType(targetResolvableType.getRawClass())));
    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
      readMethod.setAccessible(true);
    }
    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
      writeMethod.setAccessible(true);
    }
    if (isAssignable) {

      Object value = readMethod.invoke(source);
      writeMethod.invoke(target, value);

    } else {

      Object targetValue = newInstance(targetResolvableType, readMethod.invoke(source));
      writeMethod.invoke(target, targetValue);
    }
  }

  /**
   * project the property values of the given source bean into the target bean.
   * <p>Note: The property of source or target which not in json view (activeView) will silently be ignored.
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * @param source the source bean
   * @param target the target bean
   * @param activeView the active json view
   * @throws BeansException if the projecting failed
   * @see BeanWrapper
   */
  public static void projectPropertiesWithView(Object source, Object target, @Nonnull Class<?> activeView)
      throws BeansException {
    projectPropertiesWithView(source, target, null, activeView);
  }

  /**
   * project the property values of the given source bean into the given target bean,
   * only setting properties defined in the given "editable" class (or interface).
   * <p>Note: The property of source or editable which not in json view (activeView) will silently be ignored.
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * @param source the source bean
   * @param target the target bean
   * @param editable the class (or interface) to restrict property setting to
   * @param activeView the active json view
   * @throws BeansException if the projecting failed
   * @see BeanWrapper
   */
  public static void projectPropertiesWithView(Object source, Object target, @Nullable Class<?> editable,
      @Nonnull Class<?> activeView)
      throws BeansException {
    Class<?> actualEditable = Objects.nonNull(editable) ? editable : target.getClass();
    var ignoredProperties = new HashSet<String>();
    var unreadableProperties = BeanMetaUtils.forClassWithJsonView(source.getClass(), activeView)
        .getUnreadablePropertyNames();
    CollectionUtils.addAll(ignoredProperties, unreadableProperties);
    var unwritableProperties = BeanMetaUtils.forClassWithJsonView(actualEditable, activeView)
        .getUnwritablePropertyNames();
    CollectionUtils.addAll(ignoredProperties, unwritableProperties);
    projectProperties(source, target, editable, ignoredProperties);
  }

  private static Object newInstance(ResolvableType type, Object value) {
    if (value == null) {
      return null;
    }
    var cls = type.resolve();
    if (cls == null || TypeUtils.isPrimitiveOrString(cls)) {
      return value;
    }

    if (cls.isAssignableFrom(List.class)) {
      return newList(type, value);

    } else if (cls.isAssignableFrom(Map.class)) {
      return newMap(type, value);

    } else if (cls.isAssignableFrom(Set.class)) {
      return newSet(type, value);

    } else if (cls.isArray()) {
      return newArray(cls.getComponentType(), value);

    } else {
      var target = BeanUtils.instantiateClass(cls);
      projectProperties(value, target);
      return target;
    }
  }

  private static List<?> newList(ResolvableType type, Object value) {
    Collection<?> valueList = TypeUtils.cast(value);
    var list = new ArrayList<>(valueList.size());
    var genericCls = type.getGeneric(0);
    for (var v : valueList) {
      if (v == null || genericCls.isAssignableFrom(ResolvableType.forInstance(v))) {
        list.add(v);
      } else {
        list.add(newInstance(genericCls, v));
      }
    }
    return list;
  }

  private static Map<?, ?> newMap(ResolvableType type, Object value) {
    Map<?, ?> valueMap = TypeUtils.cast(value);
    var map = new HashMap<>(valueMap.size());
    var genericCls = type.getGeneric(1);
    for (var v : valueMap.entrySet()) {
      if (genericCls.isAssignableFrom(ResolvableType.forInstance(v.getValue()))) {
        map.put(v.getKey(), v.getValue());
      } else {
        map.put(v.getKey(), newInstance(genericCls, v.getValue()));
      }
    }
    return map;
  }

  private static Set<?> newSet(ResolvableType type, Object value) {
    Set<?> valueSet = TypeUtils.cast(value);
    var set = new HashSet<>(valueSet.size());
    var genericCls = type.getGeneric(0);
    for (var v : valueSet) {
      if (genericCls.isAssignableFrom(ResolvableType.forInstance(v))) {
        set.add(v);
      } else {
        set.add(newInstance(genericCls, v));
      }
    }
    return set;
  }

  private static Object newArray(Class<?> componentClass, Object value) {
    Object[] srcArr;
    var type = ResolvableType.forClass(componentClass);
    if (ClassUtils.isAssignableValue(Collection.class, value)) {
      Collection<?> col = TypeUtils.cast(value);
      srcArr = col.toArray();

    } else {
      srcArr = TypeUtils.cast(value);
    }
    Object[] destArr = TypeUtils.cast(Array.newInstance(componentClass, srcArr.length));

    for (int i = 0; i < destArr.length; i++) {
      var v = srcArr[i];
      if (v == null || type.isAssignableFrom(ResolvableType.forInstance(v))) {
        destArr[i] = v;
      } else {
        destArr[i] = newInstance(type, v);
      }
    }

    return destArr;
  }

}
