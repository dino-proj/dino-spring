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

package org.dinospring.data.converts;

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
