// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.function;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @author Cody Lu
 * @date 2022-04-16 19:01:48
 */

public interface Suppliers {

  /**
   * 静态值Supplier
   * @param <T> 值类型
   * @param value 值
   * @return Supplier
   */
  static <T> Supplier<T> ofConst(T value) {
    return () -> value;
  }

  /**
   * 生成一个Supplier，该Supplier会调用另一个Supplier的返回值，并对返回值进行Cache，如果返回值为null，则会抛出NullPointerException
   * @param <T> 返回值类型
   * @param supplier 另一个Supplier，只会被调用一次
   * @return 生成的Supplier
   */
  static <T> Supplier<T> lazy(Supplier<T> supplier) {
    return new Supplier<T>() {
      private T value;
      private volatile boolean isCached;

      @Override
      public T get() {
        if (isCached) {
          return value;
        }
        synchronized (this) {
          if (!isCached) {
            value = supplier.get();
            if (value == null) {
              throw new NullPointerException(supplier + ".get() returned null");
            } else {
              isCached = true;
            }
          }
        }
        return value;
      }
    };
  }

  /**
   * 生成一个Supplier，该Supplier会调用另一个Supplier的返回值，并对返回值进行Cache，如果Cache的Supplier返回值为null，则该Supplier返回null
   * @param <T> 返回值类型
   * @param supplier 另一个Supplier，只会被调用一次
   * @return 生成的Supplier
   */
  static <T> Supplier<T> lazyNullable(Supplier<T> supplier) {
    return new Supplier<T>() {
      private T value;
      private boolean isCached;

      @Override
      public T get() {
        if (isCached) {
          return value;
        }
        synchronized (this) {
          if (!isCached) {
            value = supplier.get();
            isCached = true;
          }
        }
        return value;
      }
    };
  }

  /**
   * 生成一个Supplier，该Supplier会调用Cache另一个Supplier的返回值，并对返回值进行Cache，如果Cache的Supplier返回值为null，则该Supplier返回defaultValue
   * @param <T> 返回值类型
   * @param supplier 另一个Supplier，只会被调用一次
   * @param defaultValue 如果supplier返回值为null，则返回此默认值
   * @return 生成的Supplier
   */
  static <T> Supplier<T> lazy(Supplier<T> supplier, T defaultValue) {
    return new Supplier<T>() {
      private T value;
      private boolean isCached;

      @Override
      public T get() {
        if (isCached) {
          return value;
        }
        synchronized (this) {
          if (!isCached) {
            value = supplier.get();
            isCached = true;
          }
        }
        return value == null ? defaultValue : value;
      }
    };
  }

  /**
   * 生成一个Supplier，该Supplier会调用Cache另一个Supplier的返回值，并对返回值进行Cache，并用predicate对cache的值进行测试，如果返回false，这重新获取
   * @param <T> 返回值类型
   * @param supplier 另一个Supplier，当predicte返回false时，会再次被调用
   * @param predicate 对Cache的值进行test，如果返回false，则重新调用supplier获取新的值
   * @return 生成的Supplier
   */
  static <T> Supplier<T> lazyPredicatable(Supplier<T> supplier, Predicate<T> predicate) {
    return new Supplier<T>() {
      private T value;
      private boolean isCached;

      @Override
      public T get() {
        var v = value;
        if (isCached && predicate.test(v)) {
          return v;
        }
        synchronized (this) {
          if (!isCached || !predicate.test(value)) {
            value = supplier.get();
            isCached = true;
          }
        }
        return value;
      }
    };
  }

}
