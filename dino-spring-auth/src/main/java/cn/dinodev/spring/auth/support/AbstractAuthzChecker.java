// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.support;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections4.CollectionUtils;
import cn.dinodev.spring.auth.AuthzChecker;
import cn.dinodev.spring.auth.annotation.CheckIgnore;
import cn.dinodev.spring.auth.session.AuthSession;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 *
 * @author Cody Lu
 * @date 2022-04-08 01:28:08
 */

public abstract class AbstractAuthzChecker<A extends Annotation, M> implements AuthzChecker {

  protected Map<AnnotatedElementKey, M> methodInvocationCache = new ConcurrentHashMap<>(64);
  protected Set<AnnotatedElementKey> noCheckMethods = new ConcurrentSkipListSet<>();
  private final Class<A> annotationClass;
  private final CheckIgnore.Type ignoreType;

  protected AbstractAuthzChecker(Class<A> annotationClass) {
    this(annotationClass, null);
  }

  protected AbstractAuthzChecker(Class<A> annotationClass, CheckIgnore.Type ignoreType) {
    this.annotationClass = annotationClass;
    this.ignoreType = ignoreType;
  }

  @Override
  public boolean support(MethodInvocation mi) {
    var methodKey = new AnnotatedElementKey(mi.getMethod(), mi.getThis().getClass());
    if (this.methodInvocationCache.containsKey(methodKey)) {
      return true;
    }
    if (this.noCheckMethods.contains(methodKey)) {
      return false;
    }
    // 检查是否忽略权限检查
    if (this.isIgnore(mi)) {
      this.noCheckMethods.add(methodKey);
      return false;
    }
    // 查询类和方法上的权限注解
    var annosInClass = AnnotatedElementUtils.findMergedRepeatableAnnotations(mi.getThis().getClass(),
        this.annotationClass);
    var annosInMethod = AnnotatedElementUtils.findMergedRepeatableAnnotations(mi.getMethod(), this.annotationClass);
    // 如果没有权限注解，则返回false
    if (CollectionUtils.isEmpty(annosInClass) && CollectionUtils.isEmpty(annosInMethod)) {
      this.noCheckMethods.add(methodKey);
      return false;
    }

    var meta = this.getMethodInvocationMeta(mi, annosInClass, annosInMethod);
    if (Objects.nonNull(meta)) {
      this.methodInvocationCache.put(methodKey, meta);
    } else {
      this.noCheckMethods.add(methodKey);
    }
    return meta != null;
  }

  protected boolean isIgnore(MethodInvocation mi) {
    // 如果没有指定忽略的类型，则不忽略
    if (Objects.isNull(this.ignoreType)) {
      return false;
    }

    // 查询方法和类上的忽略注解
    var ignoreAnno = Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(mi.getMethod(), CheckIgnore.class))
        .orElseGet(() -> AnnotatedElementUtils.findMergedAnnotation(mi.getThis().getClass(), CheckIgnore.class));

    // 如果没有忽略注解，则不忽略
    if (Objects.isNull(ignoreAnno)) {
      return false;
    }

    // 如果没有指定忽略的类型，则忽略所有类型
    if (ignoreAnno.value().length == 0) {
      return true;
    }

    // 如果指定了忽略的类型，则检查是否包含当前类型
    return Arrays.binarySearch(ignoreAnno.value(), this.ignoreType) >= 0;
  }

  /**
   * 获取方法调用的元数据
   * @param mi
   * @param annosInMethod
   * @param annosInClass
   * @return
   */
  protected abstract M getMethodInvocationMeta(MethodInvocation mi, Collection<A> annosInClass,
      Collection<A> annosInMethod);

  /**
   * 检查权限，如果权限不足，则返回false
   * @param session
   * @param mi
   * @param anno
   * @return
   */
  @Override
  public boolean isPermmited(AuthSession session, MethodInvocation mi) {
    var methodKey = new AnnotatedElementKey(mi.getMethod(), mi.getThis().getClass());
    var meta = this.methodInvocationCache.get(methodKey);
    if (Objects.isNull(meta)) {
      return true;
    }
    return this.isPermmited(session, mi, meta);
  }

  /**
   * 检查权限，如果权限不足，则返回false
   * @param session 登录会话
   * @param mi 方法调用
   * @param meta 检查器生成的元数据
   * @return
   */
  protected abstract boolean isPermmited(AuthSession session, MethodInvocation mi, M meta);

}
