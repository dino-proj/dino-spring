// Copyright 2022 dinodev.cn
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
package org.dinospring.commons.json;

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

import org.dinospring.commons.json.annotation.JsonDiscriminator;

/**
 * 实现对JsonDiscriminator注解的支持模块
 * @author Cody LU
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
      return VersionUtil.parseVersion("2.2.0", "org.dinospring", "dino-spring-commons");
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