// Copyright 2021 dinospring.cn
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

package com.botbrain.dino.utils;

import java.lang.invoke.SerializedLambda;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.reflect.MethodUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class LambdaUtils {

  /**
   * SerializedLambda 反序列化缓存
   */
  private static final Map<String, String> FUNC_PROPERTY_CACHE = new ConcurrentHashMap<>();

  public static <T, R> String methodToProperty(Function<T, R> getterFn) {

    Class<?> clazz = getterFn.getClass();
    String name = clazz.getName();
    log.info(name);
    return Optional.ofNullable(FUNC_PROPERTY_CACHE.get(name)).orElseGet(() -> {
      SerializedLambda lambda = getSerializedLambda(getterFn);
      if (lambda == null) {
        throw new IllegalArgumentException("getterFn is not lambda");
      }
      return NamingUtils.methodToProperty(lambda.getImplMethodName());
    });
  }

  public static <T, R> String methodToProperty(BiConsumer<T, R> setterFn) {

    Class<?> clazz = setterFn.getClass();
    String name = clazz.getName();
    log.info(name);
    return Optional.ofNullable(FUNC_PROPERTY_CACHE.get(name)).orElseGet(() -> {
      SerializedLambda lambda = getSerializedLambda(setterFn);
      if (lambda == null) {
        throw new IllegalArgumentException("setterFn is not lambda");
      }
      return NamingUtils.methodToProperty(lambda.getImplMethodName());
    });
  }

  /***
   * 获取类对应的Lambda
   * @param fn
   * @return
   */
  public static SerializedLambda getSerializedLambda(Object fn) {

    try {
      return (SerializedLambda) MethodUtils.invokeMethod(fn, true, "writeReplace");
    } catch (Exception e) {
      log.error("获取SerializedLambda异常, class=" + fn.getClass().getSimpleName(), e);
    }
    return null;
  }

}
