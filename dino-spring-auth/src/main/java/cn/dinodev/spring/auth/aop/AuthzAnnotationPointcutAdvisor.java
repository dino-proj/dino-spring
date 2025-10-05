// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.auth.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;

import cn.dinodev.spring.auth.annotation.CheckAuthz;
import cn.dinodev.spring.auth.annotation.CheckLoginAs;
import cn.dinodev.spring.auth.annotation.CheckPermission;
import cn.dinodev.spring.auth.annotation.CheckResource;
import cn.dinodev.spring.auth.annotation.CheckRole;
import cn.dinodev.spring.auth.session.AuthSession;

/**
 * 基于注解的权限检查的切面，将检查权限的注解转换为检查权限的切点
 * @author Cody Lu
 * @date 2022-04-07 21:26:39
 */

public class AuthzAnnotationPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

  protected static final List<Class<? extends Annotation>> AUTHZ_ANNOTATION_CLASSES = Arrays.asList(
      CheckPermission.class, CheckRole.class, CheckLoginAs.class, CheckResource.class, CheckAuthz.class);

  private transient MethodMatcher methodMatcher;

  /**
   * 创建权限注解切点通知器
   * @param beanFactory Spring Bean工厂，用于获取权限检查器实例
   */
  public AuthzAnnotationPointcutAdvisor(BeanFactory beanFactory) {
    super(new AuthzMethodInterceptor(beanFactory));
  }

  /**
   * 创建权限注解切点通知器，使用自定义的会话供应商
   * @param sessionSupplier 认证会话供应商
   * @param beanFactory Spring Bean工厂，用于获取权限检查器实例
   */
  public AuthzAnnotationPointcutAdvisor(Supplier<AuthSession> sessionSupplier, BeanFactory beanFactory) {
    super(new AuthzMethodInterceptor(sessionSupplier, beanFactory));
  }

  /**
   * 创建权限注解切点通知器，使用自定义的方法匹配器
   * @param methodMatcher 自定义的方法匹配器
   * @param beanFactory Spring Bean工厂，用于获取权限检查器实例
   */
  public AuthzAnnotationPointcutAdvisor(MethodMatcher methodMatcher, BeanFactory beanFactory) {
    super(new AuthzMethodInterceptor(beanFactory));
    this.methodMatcher = methodMatcher;
  }

  @Override
  public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
    if (this.isAuthzAnnotationPresent(method)) {
      return this.secondaryMatch(method, targetClass);
    }
    if (Objects.nonNull(targetClass)) {
      if (this.isAuthzAnnotationPresent(targetClass)) {
        return this.secondaryMatch(method, targetClass);
      }
      // check the implement method of the target class
      var implementMethod = MethodUtils.getAccessibleMethod(targetClass, method.getName(), method.getParameterTypes());
      if (this.isAuthzAnnotationPresent(implementMethod)) {
        return this.secondaryMatch(method, targetClass);
      }
    }
    return false;
  }

  /**
   * 检查指定的类是否存在权限注解
   * @param clss 要检查的类
   * @return 如果类上存在任何权限相关注解则返回true，否则返回false
   */
  protected boolean isAuthzAnnotationPresent(Class<?> clss) {
    for (Class<? extends Annotation> annoClss : AUTHZ_ANNOTATION_CLASSES) {
      if (AnnotatedElementUtils.hasAnnotation(clss, annoClss)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 检查指定的方法是否存在权限注解
   * @param method 要检查的方法
   * @return 如果方法上存在任何权限相关注解则返回true，否则返回false
   */
  protected boolean isAuthzAnnotationPresent(Method method) {
    if (Objects.isNull(method)) {
      return false;
    }
    for (Class<? extends Annotation> annoClss : AUTHZ_ANNOTATION_CLASSES) {
      if (AnnotatedElementUtils.hasAnnotation(method, annoClss)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 执行二次匹配检查
   * 
   * <p>如果配置了自定义的方法匹配器，则使用该匹配器进行进一步的匹配检查；
   * 否则直接返回true，表示匹配成功。</p>
   * 
   * @param method 要检查的方法
   * @param targetClass 目标类
   * @return 如果匹配则返回true，否则返回false
   */
  protected boolean secondaryMatch(Method method, Class<?> targetClass) {
    if (Objects.nonNull(this.methodMatcher)) {
      return this.methodMatcher.matches(method, targetClass);
    }
    return true;
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 10_000;
  }
}
