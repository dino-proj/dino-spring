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

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter(autoApply = true)
public class JsonStringConvert implements AttributeConverter<JsonNode, Object> {
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Object convertToDatabaseColumn(JsonNode attribute) {
    return attribute;
  }

  @Override
  public JsonNode convertToEntityAttribute(Object dbData) {
    if (Objects.isNull(dbData)) {
      return null;
    }
    try {
      return objectMapper.readTree(objectMapper.writeValueAsString(dbData));
    } catch (JsonProcessingException e) {
      log.error("convert error:{}", dbData, e);
    }
    return null;
  }

}
