// Copyright 2022 dinospring.cn
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

package org.dinospring.commons.bean;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 * @date 2022-05-30 11:57:57
 */

@Slf4j
public class BeanSafeCache<T> {
  /**
   * Map keyed by Class containing <T>, strongly held.
   * This variant is being used for cache-safe bean classes.
   */
  private final ConcurrentMap<Class<?>, T> strongClassCache = new ConcurrentHashMap<>(64);

  /**
  * Map keyed by Class containing <T>, softly held.
  * This variant is being used for non-cache-safe bean classes.
  */
  private final ConcurrentMap<Class<?>, T> softClassCache = new ConcurrentReferenceHashMap<>(64);

  /**
   * Set of ClassLoaders that this CachedIntrospectionResults class will always
   * accept classes from, even if the classes do not qualify as cache-safe.
   */
  private final Set<ClassLoader> acceptedClassLoaders = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

  /**
   * Accept the given ClassLoader as cache-safe, even if its classes would
   * not qualify as cache-safe in this CachedIntrospectionResults class.
   * <p>This configuration method is only relevant in scenarios where the Spring
   * classes reside in a 'common' ClassLoader (e.g. the system ClassLoader)
   * whose lifecycle is not coupled to the application. In such a scenario,
   * CachedIntrospectionResults would by default not cache any of the application's
   * classes, since they would create a leak in the common ClassLoader.
   * <p>Any {@code acceptClassLoader} call at application startup should
   * be paired with a {@link #clearClassLoader} call at application shutdown.
   * @param classLoader the ClassLoader to accept
   */
  public void acceptClassLoader(@Nullable ClassLoader classLoader) {
    if (classLoader != null) {
      acceptedClassLoaders.add(classLoader);
    }
  }

  /**
   * Clear the introspection cache for the given ClassLoader, removing the
   * introspection results for all classes underneath that ClassLoader, and
   * removing the ClassLoader (and its children) from the acceptance list.
   * @param classLoader the ClassLoader to clear the cache for
   */
  public void clearClassLoader(@Nullable ClassLoader classLoader) {
    acceptedClassLoaders.removeIf(registeredLoader -> isUnderneathClassLoader(registeredLoader, classLoader));
    strongClassCache.keySet().removeIf(beanClass -> isUnderneathClassLoader(beanClass.getClassLoader(), classLoader));
    softClassCache.keySet().removeIf(beanClass -> isUnderneathClassLoader(beanClass.getClassLoader(), classLoader));
  }

  /**
   * Check whether this CachedIntrospectionResults class is configured
   * to accept the given ClassLoader.
   * @param classLoader the ClassLoader to check
   * @return whether the given ClassLoader is accepted
   * @see #acceptClassLoader
   */
  public boolean isClassLoaderAccepted(ClassLoader classLoader) {
    for (ClassLoader acceptedLoader : acceptedClassLoaders) {
      if (isUnderneathClassLoader(classLoader, acceptedLoader)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieve a cached info<T> for the given target class.
   * @param beanClass the target class to introspect
   * @return the resulting cached info<T> (never {@code null})
   */
  public T get(Class<?> beanClass) {
    T results = strongClassCache.get(beanClass);
    if (Objects.nonNull(results)) {
      return results;
    }
    return softClassCache.get(beanClass);
  }

  /**
   * Retrieve a cached info<T> for the given target class.
   * @param beanClass the target class to introspect
   * @return the resulting cached info<T> (never {@code null})
   */
  public T getOrElse(Class<?> beanClass, Function<Class<?>, ? extends T> provider) {
    T results = get(beanClass);
    if (Objects.nonNull(results)) {
      return results;
    }

    results = provider.apply(beanClass);
    if (Objects.nonNull(results)) {
      put(beanClass, results);
    }
    return results;
  }

  /**
   * Associates the specified value with the specified key in this cache.
   * @param beanClass
   * @param info
   * @return the previous value associated with key, or null if there was no mapping for key.
   */
  public T put(Class<?> beanClass, T info) {
    ConcurrentMap<Class<?>, T> classCacheToUse;

    if (ClassUtils.isCacheSafe(beanClass, BeanSafeCache.class.getClassLoader()) ||
        isClassLoaderAccepted(beanClass.getClassLoader())) {
      classCacheToUse = strongClassCache;
    } else {
      if (log.isDebugEnabled()) {
        log.debug("Not strongly caching class [{}] because it is not cache-safe", beanClass.getName());
      }
      classCacheToUse = softClassCache;
    }

    return classCacheToUse.put(beanClass, info);
  }

  /**
   * remove the specified key in this cache.
   * @param beanClass
   * @return the previous value associated with key, or null if there was no mapping for key.
   */
  public T remove(Class<?> beanClass) {
    var results = strongClassCache.remove(beanClass);

    if (Objects.nonNull(results)) {
      return results;
    }

    return softClassCache.remove(beanClass);
  }

  /**
   * Check whether the given ClassLoader is underneath the given parent,
   * that is, whether the parent is within the candidate's hierarchy.
   * @param candidate the candidate ClassLoader to check
   * @param parent the parent ClassLoader to check for
   */
  private boolean isUnderneathClassLoader(@Nullable ClassLoader candidate, @Nullable ClassLoader parent) {
    if (candidate == parent) {
      return true;
    }
    if (candidate == null) {
      return false;
    }
    ClassLoader classLoaderToCheck = candidate;
    while (classLoaderToCheck != null) {
      classLoaderToCheck = classLoaderToCheck.getParent();
      if (classLoaderToCheck == parent) {
        return true;
      }
    }
    return false;
  }

}
