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

package org.dinospring.auth.support;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.auth.AuthzChecker;
import org.dinospring.auth.annotation.CheckIgnore;
import org.dinospring.auth.session.AuthSession;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 *
 * @author tuuboo
 * @date 2022-04-08 01:28:08
 */

public abstract class AbstractAuthzChecker<A extends Annotation, M> implements AuthzChecker {

  protected Map<AnnotatedElementKey, M> methodInvocationCache = new ConcurrentHashMap<>(64);
  protected Set<AnnotatedElementKey> noCheckMethods = new ConcurrentSkipListSet<>();
  private final Class<A> annotationClass;
  private final boolean enableCheckIgnoreAnnotation;

  protected AbstractAuthzChecker(Class<A> annotationClass, boolean enableCheckIgnoreAnnotation) {
    this.annotationClass = annotationClass;
    this.enableCheckIgnoreAnnotation = enableCheckIgnoreAnnotation;
  }

  @Override
  public boolean support(MethodInvocation mi) {
    var methodKey = new AnnotatedElementKey(mi.getMethod(), mi.getThis().getClass());
    if (methodInvocationCache.containsKey(methodKey)) {
      return true;
    }
    if (noCheckMethods.contains(methodKey)) {
      return false;
    }
    // ??????????????????????????????
    if (isIgnore(mi)) {
      noCheckMethods.add(methodKey);
      return false;
    }
    // ????????????????????????????????????
    var annosInClass = AnnotatedElementUtils.findMergedRepeatableAnnotations(mi.getThis().getClass(), annotationClass);
    var annosInMethod = AnnotatedElementUtils.findMergedRepeatableAnnotations(mi.getMethod(), annotationClass);
    // ????????????????????????????????????false
    if (CollectionUtils.isEmpty(annosInClass) && CollectionUtils.isEmpty(annosInMethod)) {
      noCheckMethods.add(methodKey);
      return false;
    }

    var meta = getMethodInvocationMeta(mi, annosInClass, annosInMethod);
    if (Objects.nonNull(meta)) {
      methodInvocationCache.put(methodKey, meta);
    } else {
      noCheckMethods.add(methodKey);
    }
    return meta != null;
  }

  protected boolean isIgnore(MethodInvocation mi) {
    if (!enableCheckIgnoreAnnotation) {
      return false;
    }
    return AnnotatedElementUtils.hasAnnotation(mi.getThis().getClass(), CheckIgnore.class)
        || AnnotatedElementUtils.hasAnnotation(mi.getMethod(), CheckIgnore.class);
  }

  /**
   * ??????????????????????????????
   * @param mi
   * @param annosInMethod
   * @param annosInClass
   * @return
   */
  protected abstract M getMethodInvocationMeta(MethodInvocation mi, Collection<A> annosInClass,
      Collection<A> annosInMethod);

  /**
   * ?????????????????????????????????????????????false
   * @param session
   * @param mi
   * @param anno
   * @return
   */
  @Override
  public boolean isPermmited(AuthSession session, MethodInvocation mi) {
    var methodKey = new AnnotatedElementKey(mi.getMethod(), mi.getThis().getClass());
    var meta = methodInvocationCache.get(methodKey);
    if (Objects.isNull(meta)) {
      return true;
    }
    return isPermmited(session, mi, meta);
  }

  /**
   * ?????????????????????????????????????????????false
   * @param session ????????????
   * @param mi ????????????
   * @param meta ???????????????????????????
   * @return
   */
  protected abstract boolean isPermmited(AuthSession session, MethodInvocation mi, M meta);

}
