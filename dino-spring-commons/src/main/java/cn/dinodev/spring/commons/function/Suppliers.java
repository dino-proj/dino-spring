// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.function;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 供应者工具接口，提供各种Supplier实现
 * <p>
 * 该接口提供了多种有用的Supplier工具方法，包括：
 * <ul>
 * <li>常量Supplier - 总是返回相同值的Supplier</li>
 * <li>延迟Supplier - 只在首次调用时执行计算并缓存结果</li>
 * <li>可空延迟Supplier - 允许缓存null值的延迟Supplier</li>
 * <li>条件Supplier - 基于条件判断返回不同值的Supplier</li>
 * </ul>
 * </p>
 *
 * @author Cody Lu
 * @since 2022-04-16
 */

public interface Suppliers {

  /**
   * 创建一个静态值Supplier
   * <p>
   * 返回的Supplier总是返回相同的值，无论调用多少次。
   * 这对于需要Supplier接口但值不会变化的场景很有用。
   * </p>
   *
   * @param <T> 值类型
   * @param value 要返回的常量值
   * @return 总是返回指定值的Supplier
   */
  static <T> Supplier<T> ofConst(T value) {
    return () -> value;
  }

  /**
   * 创建一个延迟计算的Supplier（不允许null值）
   * <p>
   * 返回的Supplier会在首次调用get()时执行原始supplier的计算，
   * 并将结果缓存起来，后续调用直接返回缓存的值。
   * </p>
   * <p>
   * 特性：
   * <ul>
   * <li>线程安全：使用ReentrantLock确保线程安全</li>
   * <li>延迟计算：只在需要时才执行计算</li>
   * <li>缓存结果：避免重复计算</li>
   * <li>不允许null：如果supplier返回null会抛出异常</li>
   * </ul>
   * </p>
   *
   * @param <T> 返回值类型
   * @param supplier 原始Supplier，只会被调用一次
   * @return 带缓存功能的延迟Supplier
   * @throws IllegalStateException 如果supplier返回null值
   */
  static <T> Supplier<T> lazy(Supplier<T> supplier) {
    return new Supplier<>() {
      private T value;
      private volatile boolean isCached;
      private final ReentrantLock lock = new ReentrantLock();

      @Override
      public T get() {
        if (isCached) {
          return value; // 快速路径：如果已缓存，直接返回
        }
        lock.lock();
        try {
          if (!isCached) {
            value = supplier.get();
            if (value == null) {
              // 抛出IllegalStateException而不是NullPointerException
              // 因为这是业务逻辑错误，不是编程错误
              throw new IllegalStateException(supplier + ".get() returned null");
            } else {
              isCached = true;
            }
          }
        } finally {
          lock.unlock();
        }
        return value;
      }
    };
  }

  /**
   * 创建一个延迟计算的Supplier（允许null值）
   * <p>
   * 与{@link #lazy(Supplier)}类似，但允许缓存null值。
   * 返回的Supplier会在首次调用get()时执行原始supplier的计算，
   * 并将结果（包括null）缓存起来，后续调用直接返回缓存的值。
   * </p>
   * <p>
   * 特性：
   * <ul>
   * <li>线程安全：使用ReentrantLock确保线程安全</li>
   * <li>延迟计算：只在需要时才执行计算</li>
   * <li>缓存结果：避免重复计算</li>
   * <li>允许null：可以缓存和返回null值</li>
   * </ul>
   * </p>
   *
   * @param <T> 返回值类型
   * @param supplier 原始Supplier，只会被调用一次
   * @return 带缓存功能的延迟Supplier，允许null值
   */
  static <T> Supplier<T> lazyNullable(Supplier<T> supplier) {
    return new Supplier<>() {
      private T value;
      private volatile boolean isCached;
      private final ReentrantLock lock = new ReentrantLock();

      @Override
      public T get() {
        if (isCached) {
          return value;
        }
        lock.lock();
        try {
          if (!isCached) {
            value = supplier.get();
            isCached = true;
          }
        } finally {
          lock.unlock();
        }
        return value;
      }
    };
  }

  /**
   * 创建一个延迟计算的Supplier（带默认值）
   * <p>
   * 与{@link #lazyNullable(Supplier)}类似，但当supplier返回null时，
   * 会返回指定的默认值而不是null。
   * </p>
   *
   * @param <T> 返回值类型
   * @param supplier 原始Supplier，只会被调用一次
   * @param defaultValue 如果supplier返回null时的默认值
   * @return 带缓存功能和默认值的延迟Supplier
   */
  static <T> Supplier<T> lazy(Supplier<T> supplier, T defaultValue) {
    return new Supplier<>() {
      private T value;
      private volatile boolean isCached;
      private final ReentrantLock lock = new ReentrantLock();

      @Override
      public T get() {
        if (isCached) {
          return value;
        }
        lock.lock();
        try {
          if (!isCached) {
            value = supplier.get();
            isCached = true;
          }
        } finally {
          lock.unlock();
        }
        return value == null ? defaultValue : value;
      }
    };
  }

  /**
   * 创建一个可重新验证的延迟Supplier
   * <p>
   * 该Supplier会缓存值，但使用predicate来验证缓存的值是否仍然有效。
   * 如果验证失败，会重新调用原始supplier获取新值。
   * </p>
   *
   * @param <T> 返回值类型
   * @param supplier 原始Supplier，可能被多次调用
   * @param predicate 用于验证缓存值的断言，返回false时会重新获取值
   * @return 带验证功能的延迟Supplier
   */
  static <T> Supplier<T> lazyPredicatable(Supplier<T> supplier, Predicate<T> predicate) {
    return new Supplier<>() {
      private T value;
      private volatile boolean isCached;
      private final ReentrantLock lock = new ReentrantLock();

      @Override
      public T get() {
        T currentValue = value;
        if (isCached && predicate.test(currentValue)) {
          return currentValue;
        }
        lock.lock();
        try {
          if (!isCached || !predicate.test(value)) {
            value = supplier.get();
            isCached = true;
          }
        } finally {
          lock.unlock();
        }
        return value;
      }
    };
  }

}
