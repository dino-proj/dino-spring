// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.converts;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 */

@Slf4j
@Converter(autoApply = true)
public class JsonStringConvert implements AttributeConverter<JsonNode, String> {
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public String convertToDatabaseColumn(JsonNode attribute) {
    return attribute.asText();
  }

  @Override
  public JsonNode convertToEntityAttribute(String dbData) {
    if (Objects.isNull(dbData)) {
      return NullNode.getInstance();
    }
    try {
      return objectMapper.readTree(dbData);
    } catch (JsonProcessingException e) {
      log.error("convert error:{}", dbData, e);
    }
    return null;
  }

}
