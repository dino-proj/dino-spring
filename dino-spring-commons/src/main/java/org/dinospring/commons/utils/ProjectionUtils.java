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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;

import lombok.experimental.UtilityClass;

/**
 *
 * @author tuuboo
 */

@UtilityClass
public class ProjectionUtils {

  /**
   * Copy the property values of the given source bean into the target bean.
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * <p>This is just a convenience method. For more complex transfer needs,
   * consider using a full BeanWrapper.
   * @param source the source bean
   * @param target the target bean
   * @throws BeansException if the copying failed
   * @see BeanWrapper
   */
  public static void projectProperties(Object source, Object target) throws BeansException {
    projectProperties(source, target, null, (String[]) null);
  }

  /**
   * Copy the property values of the given source bean into the given target bean,
   * only setting properties defined in the given "editable" class (or interface).
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * <p>This is just a convenience method. For more complex transfer needs,
   * consider using a full BeanWrapper.
   * @param source the source bean
   * @param target the target bean
   * @param editable the class (or interface) to restrict property setting to
   * @throws BeansException if the copying failed
   * @see BeanWrapper
   */
  public static void projectProperties(Object source, Object target, Class<?> editable) throws BeansException {
    projectProperties(source, target, editable, (String[]) null);
  }

  /**
   * Copy the property values of the given source bean into the given target bean,
   * ignoring the given "ignoreProperties".
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * <p>This is just a convenience method. For more complex transfer needs,
   * consider using a full BeanWrapper.
   * @param source the source bean
   * @param target the target bean
   * @param ignoreProperties array of property names to ignore
   * @throws BeansException if the copying failed
   * @see BeanWrapper
   */
  public static void projectProperties(Object source, Object target, String... ignoreProperties) throws BeansException {
    projectProperties(source, target, null, ignoreProperties);
  }

  /**
   * Copy the property values of the given source bean into the given target bean.
   * <p>Note: The source and target classes do not have to match or even be derived
   * from each other, as long as the properties match. Any bean properties that the
   * source bean exposes but the target bean does not will silently be ignored.
   * <p>As of Spring Framework 5.3, this method honors generic type information
   * when matching properties in the source and target objects.
   * @param source the source bean
   * @param target the target bean
   * @param editable the class (or interface) to restrict property setting to
   * @param ignoreProperties array of property names to ignore
   * @throws BeansException if the copying failed
   * @see BeanWrapper
   */
  private static void projectProperties(Object source, Object target, @Nullable Class<?> editable,
      @Nullable String... ignoreProperties) throws BeansException {

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
    List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : Collections.emptyList());

    for (PropertyDescriptor targetPd : targetPds) {
      Method writeMethod = targetPd.getWriteMethod();
      if (writeMethod == null || ignoreList.contains(targetPd.getName())) {
        continue;
      }
      PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
      if (sourcePd == null || sourcePd.getReadMethod() == null) {
        continue;
      }
      Method readMethod = sourcePd.getReadMethod();
      ResolvableType sourceResolvableType = ResolvableType.forMethodReturnType(readMethod);
      ResolvableType targetResolvableType = ResolvableType.forMethodParameter(writeMethod, 0);

      // Ignore generic types in assignable check if either ResolvableType has unresolvable generics.
      boolean isAssignable = (sourceResolvableType.hasUnresolvableGenerics()
          || targetResolvableType.hasUnresolvableGenerics()
              ? ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())
              : targetResolvableType.isAssignableFrom(sourceResolvableType));

      try {
        if (isAssignable) {
          if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
            readMethod.setAccessible(true);
          }
          Object value = readMethod.invoke(source);
          if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
            writeMethod.setAccessible(true);
          }
          writeMethod.invoke(target, value);

        } else if (!BeanUtils.isSimpleValueType(targetResolvableType.getRawClass())) {
          if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
            readMethod.setAccessible(true);
          }
          Object targetValue = BeanUtils.instantiateClass(targetResolvableType.resolve());
          projectProperties(readMethod.invoke(source), targetValue);
          if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
            writeMethod.setAccessible(true);
          }
          writeMethod.invoke(target, targetValue);
        }
      } catch (Throwable ex) {
        throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
      }
    }
  }

}