// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.projection;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.dinospring.commons.bean.BeanMetaUtils;
import org.dinospring.commons.function.Functions;
import org.dinospring.commons.json.JsonViewUtils;
import org.dinospring.commons.utils.TypeUtils;
import org.springframework.core.convert.ConversionService;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 *
 * @author Cody LU
 * @date 2022-06-09 01:15:01
 */

public class PropertyProjector<S, T> {

  private final Class<S> sourceClass;
  private final Class<T> targetClass;
  private ConversionService conversionService;

  /**
   * @param sourceClass
   * @param targetClass
   */
  public PropertyProjector(@Nonnull Class<S> sourceClass, @Nonnull Class<T> targetClass) {
    this(sourceClass, targetClass, null);
  }

  /**
   * @param sourceClass
   * @param targetClass
   * @param conversionService
   */
  public PropertyProjector(@Nonnull Class<S> sourceClass, @Nonnull Class<T> targetClass,
      @Nullable ConversionService conversionService) {
    this.sourceClass = sourceClass;
    this.targetClass = targetClass;
    this.conversionService = conversionService;
  }

  @Nullable
  public ConversionService getConversionService() {
    return this.conversionService;
  }

  public void setConversionService(@Nullable ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  private List<PropertyCopier> copiers = new ArrayList<>(4);

  public T copy(S source) {
    T target = TypeUtils.newInstance(this.targetClass);
    return this.copy(source, target);
  }

  public T copy(S source, Class<?> activeView) {
    T target = TypeUtils.newInstance(this.targetClass);
    return this.copy(source, target, activeView);
  }

  public T copy(S source, T target) {
    for (PropertyCopier copier : this.copiers) {
      copier.copy(source, target);
    }
    return target;
  }

  public T copy(S source, T target, Class<?> activeView) {
    for (PropertyCopier copier : this.copiers) {
      if (copier.canCopy(activeView)) {
        copier.copy(source, target);
      }
    }
    return target;
  }

  public T copy(S source, Function<S, T> targetSupplier) {
    T target = targetSupplier.apply(source);
    return this.copy(source, target);
  }

  public T copy(S source, Function<S, T> targetSupplier, Class<?> activeView) {
    T target = targetSupplier.apply(source);
    return this.copy(source, target, activeView);
  }

  public PropertyProjector<S, T> add(Method sourceGetter, Method targetSetter) {
    return this.add(sourceGetter, targetSetter, Functions.identity());
  }

  public PropertyProjector<S, T> add(Method sourceGetter, Method targetSetter, Function<?, ?> converter) {
    this.copiers.add(new PropertyCopier(t -> {
      try {
        return sourceGetter.invoke(t);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        return null;
      }
    }, (t, u) -> {
      try {
        targetSetter.invoke(t, converter.apply(TypeUtils.cast(u)));
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    },
        JsonViewUtils.findViews(sourceGetter),
        JsonViewUtils.findViews(targetSetter)));
    return this;
  }

  public <V> PropertyProjector<S, T> add(Method sourceGetter, BiConsumer<T, V> setter) {
    return this.add(this.makeGetter(sourceGetter), setter, Functions.identity(), JsonViewUtils.findViews(sourceGetter));
  }

  public PropertyProjector<S, T> add(String sourcePropertyName, String targetPropertyName) {
    var getter = BeanMetaUtils.forClass(this.sourceClass).getProperty(sourcePropertyName);
    if (Objects.isNull(getter)) {
      throw new IllegalArgumentException("source property not found: " + sourcePropertyName);
    }
    var setter = BeanMetaUtils.forClass(this.targetClass).getProperty(targetPropertyName);
    if (Objects.isNull(setter)) {
      throw new IllegalArgumentException("target property not found: " + targetPropertyName);
    }
    return this.add(getter.getReadMethod(), setter.getWriteMethod());
  }

  public PropertyProjector<S, T> add(String sourcePropertyName, String targetPropertyName, Function<?, ?> converter) {
    var getter = BeanMetaUtils.forClass(this.sourceClass).getProperty(sourcePropertyName);
    if (Objects.isNull(getter)) {
      throw new IllegalArgumentException("source property not found: " + sourcePropertyName);
    }
    var setter = BeanMetaUtils.forClass(this.targetClass).getProperty(targetPropertyName);
    if (Objects.isNull(setter)) {
      throw new IllegalArgumentException("target property not found: " + targetPropertyName);
    }
    return this.add(getter.getReadMethod(), setter.getWriteMethod(), converter);
  }

  public PropertyProjector<S, T> add(PropertyDescriptor getter, String targetPropertyName,
      Function<?, ?> converter) {
    var setter = BeanMetaUtils.forClass(this.targetClass).getProperty(targetPropertyName);
    if (Objects.isNull(setter)) {
      throw new IllegalArgumentException("target property not found: " + targetPropertyName);
    }
    return this.add(getter.getReadMethod(), setter.getWriteMethod(), converter);
  }

  public PropertyProjector<S, T> add(PropertyDescriptor getter, PropertyDescriptor setter,
      Supplier<Function<?, ?>> converter) {
    return this.add(getter.getReadMethod(), setter.getWriteMethod(), converter.get());
  }

  public PropertyProjector<S, T> add(PropertyDescriptor getter, PropertyDescriptor setter) {
    return this.add(getter.getReadMethod(), setter.getWriteMethod());
  }

  public <V> PropertyProjector<S, T> add(String sourcePropertyName, BiConsumer<T, V> setter) {
    var getter = BeanMetaUtils.forClass(this.sourceClass).getProperty(sourcePropertyName);
    if (Objects.isNull(getter)) {
      throw new IllegalArgumentException("source property not found: " + sourcePropertyName);
    }
    return this.add(getter.getReadMethod(), setter);
  }

  public <V> PropertyProjector<S, T> add(String sourcePropertyName, BiConsumer<T, V> setter,
      Function<V, V> converter) {
    var getter = BeanMetaUtils.forClass(this.sourceClass).getProperty(sourcePropertyName);
    if (Objects.isNull(getter)) {
      throw new IllegalArgumentException("source property not found: " + sourcePropertyName);
    }
    return this.add(this.makeGetter(getter.getReadMethod()), setter, converter,
        JsonViewUtils.findViews(getter.getReadMethod()));
  }

  public <V> PropertyProjector<S, T> add(Function<S, V> getter, BiConsumer<T, V> setter) {
    this.copiers.add(new PropertyCopier(getter, setter));
    return this;
  }

  public <V> PropertyProjector<S, T> add(Function<S, V> getter, BiConsumer<T, V> setter, Class<?>... views) {
    this.copiers.add(new PropertyCopier(getter, setter, views));
    return this;
  }

  public <V> PropertyProjector<S, T> add(Function<S, V> getter, BiConsumer<T, V> setter, Class<?>[] getterViews,
      Class<?>[] setterViews) {
    this.copiers.add(new PropertyCopier(getter, setter, getterViews, setterViews));
    return this;
  }

  public <V> PropertyProjector<S, T> add(Function<S, V> getter, BiConsumer<T, V> setter, Function<V, V> converter) {
    return this.add(getter, (t, u) -> {
      setter.accept(t, converter.apply(TypeUtils.cast(u)));
    });
  }

  public <V> PropertyProjector<S, T> add(Function<S, V> getter, BiConsumer<T, V> setter, Function<V, V> converter,
      Class<?>... views) {
    if (Objects.isNull(converter)) {
      return this.add(getter, setter, views);
    }
    return this.add(getter, (t, u) -> {
      setter.accept(t, converter.apply(TypeUtils.cast(u)));
    }, views);
  }

  private <V> Function<S, V> makeGetter(Method readMethod) {
    return s -> {
      try {
        return TypeUtils.cast(readMethod.invoke(s));
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    };
  }

  private class PropertyCopier {
    private final Function<S, ?> getter;
    private final BiConsumer<T, ?> setter;

    private final Class<?>[] views;

    private final Class<?>[] getterViews;
    private final Class<?>[] setterViews;

    private PropertyCopier(Function<S, ?> getter, BiConsumer<T, ?> setter) {
      this(getter, setter, null);

    }

    private PropertyCopier(Function<S, ?> getter, BiConsumer<T, ?> setter, Class<?>[] views) {
      this.getter = getter;
      this.setter = setter;
      this.views = views;
      this.getterViews = null;
      this.setterViews = null;

    }

    public PropertyCopier(Function<S, ?> getter, BiConsumer<T, ?> setter, Class<?>[] getterViews,
        Class<?>[] setterViews) {
      this.getter = getter;
      this.setter = setter;
      this.views = null;
      this.getterViews = getterViews;
      this.setterViews = setterViews;
    }

    public void copy(S source, T target) {
      this.setter.accept(target, TypeUtils.cast(this.getter.apply(source)));
    }

    public boolean canCopy(Class<?> activeView) {
      if (Objects.isNull(activeView)) {
        return true;
      }
      if (Objects.nonNull(this.views)) {
        return JsonViewUtils.isInView(activeView, this.views);
      }
      return JsonViewUtils.isInView(activeView, this.getterViews)
          && JsonViewUtils.isInView(activeView, this.setterViews);
    }
  }

}
