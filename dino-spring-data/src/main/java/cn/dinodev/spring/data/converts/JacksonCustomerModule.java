// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.converts;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.internal.bind.TypeAdapters;

/**
 * Gson和Jackson的转换
 * @author Cody Lu
 */
public final class JacksonCustomerModule extends SimpleModule {

  /**
   * 私有构造函数，防止直接实例化
   */
  private JacksonCustomerModule() {
    super();
  }

  /**
   * 创建JacksonCustomerModule实例并配置JSON元素反序列化器
   * @return 配置好的JacksonCustomerModule实例
   */
  public static JacksonCustomerModule create() {
    JacksonCustomerModule module = new JacksonCustomerModule();
    module.configureDeserializers();
    return module;
  }

  private void configureDeserializers() {
    this.addDeserializer(JsonElement.class, new JsonDeserializer<>() {
      private final JsonDeserializer<?> delegate = new UntypedObjectDeserializer(null, null);

      @Override
      public JsonElement deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        Object val = delegate.deserialize(parser, ctxt);
        if (val == null) {
          return JsonNull.INSTANCE;
        }
        return TypeAdapters.JSON_ELEMENT.fromJson(new Gson().toJson(val));
      }
    });
  }
}
