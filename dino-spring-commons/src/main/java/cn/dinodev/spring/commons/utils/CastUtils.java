// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.utils;

/**
 * SpringFramework 6.0 移除 CastUtils, 这里重新定义一个
 * 
 * @author Cody Lu
 * @since 2025-09-18
 */
public interface CastUtils {

  /**
   * 将对象强制转换为指定类型
   *
   * @param <T> 目标类型的泛型参数
   * @param object 需要转换的对象，可以为null
   * @return 转换后的对象，类型为T；如果输入为null则返回null
   * @throws ClassCastException 如果对象无法转换为目标类型时抛出此异常
   *
   * <p><strong>注意：</strong>此方法会抑制unchecked警告，使用时需要确保类型转换的安全性</p>
   * <p><strong>实现说明：</strong>该方法直接进行强制类型转换，不进行类型检查，调用者需要保证类型兼容性</p>
   */
  @SuppressWarnings("unchecked")
  public static <T> T cast(Object object) {
    return (T) object;
  }
}
