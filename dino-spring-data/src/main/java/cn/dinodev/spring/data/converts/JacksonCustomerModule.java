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
public class JacksonCustomerModule extends SimpleModule {
  public JacksonCustomerModule() {
    this.addDeserializer(JsonElement.class, new JsonDeserializer<JsonElement>() {
      private final JsonDeserializer<?> delegate = new UntypedObjectDeserializer(null, null);

      @Override
      public JsonElement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Object val = delegate.deserialize(p, ctxt);
        if (val == null) {
          return JsonNull.INSTANCE;
        }
        return TypeAdapters.JSON_ELEMENT.fromJson(new Gson().toJson(val));
      }
    });
  }
}
