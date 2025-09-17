// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0
package cn.dinodev.spring.commons.json;

import java.lang.annotation.Annotation;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.ClassUtil;

import cn.dinodev.spring.commons.json.annotation.JsonDiscriminator;

/**
 * 实现对JsonDiscriminator注解的支持模块
 * @author Cody Lu
 * @date 2022-04-23 21:35:21
 */

public class JsonDiscriminatorModule extends SimpleModule {

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);
    context.insertAnnotationIntrospector(new JsonDiscriminatorAnnotationIntrospector());
  }

  public static class JsonDiscriminatorAnnotationIntrospector extends AnnotationIntrospector {

    @Override
    public Version version() {
      return VersionUtil.parseVersion("2.2.0", "cn.dinodev.spring", "dino-spring-commons");
    }

    @Override
    public boolean isAnnotationBundle(Annotation ann) {
      return ann.annotationType().equals(JsonDiscriminator.class);
    }

    @Override
    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass am,
        JavaType baseType) {
      if (baseType.isContainerType() || baseType.isReferenceType()) {
        return null;
      }
      var jd = _findAnnotation(am, JsonDiscriminator.class);
      if (Objects.isNull(jd)) {
        return null;
      }

      JsonTypeInfo info = jd.annotationType().getAnnotation(JsonTypeInfo.class);
      JsonTypeIdResolver idResInfo = jd.annotationType().getAnnotation(JsonTypeIdResolver.class);
      TypeIdResolver idRes = (idResInfo == null) ? null
          : (TypeIdResolver) ClassUtil.createInstance(idResInfo.value(), config.canOverrideAccessModifiers());
      if (idRes != null) {
        idRes.init(baseType);
      }
      var builder = new StdTypeResolverBuilder();
      builder = builder.init(info.use(), idRes);

      JsonTypeInfo.As inclusion = info.include();
      if (inclusion == JsonTypeInfo.As.EXTERNAL_PROPERTY) {
        inclusion = JsonTypeInfo.As.PROPERTY;
      }
      builder = builder.inclusion(inclusion);
      builder = builder.typeProperty(jd.property());
      Class<?> defaultImpl = info.defaultImpl();

      if (!defaultImpl.isAnnotation()) {
        builder = builder.defaultImpl(defaultImpl);
      }
      builder = builder.typeIdVisibility(info.visible());
      return builder;
    }
  }
}