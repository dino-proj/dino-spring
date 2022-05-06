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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.vladmihalcea.hibernate.type.util.ObjectMapperSupplier;

import org.dinospring.commons.json.JsonDiscriminatorModule;

/**
 *
 * @author tuuboo
 */

public class CustomObjectMapperSupplier implements ObjectMapperSupplier {

  @Override
  public ObjectMapper get() {
    var builder = JsonMapper.builder();
    builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    builder.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    builder.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    builder.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    builder.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    builder.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);

    var objectMapper = builder.build();

    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    objectMapper.registerModule(new JacksonCustomerModule());
    objectMapper.registerModule(new JsonDiscriminatorModule());
    return objectMapper;
  }

}
