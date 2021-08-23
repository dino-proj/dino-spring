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
