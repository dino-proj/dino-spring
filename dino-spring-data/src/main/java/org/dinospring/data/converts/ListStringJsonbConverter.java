// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.converts;

import java.sql.SQLException;
import java.util.List;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author Cody Lu
 */

@Component
@Converter(autoApply = true)
public class ListStringJsonbConverter implements AttributeConverter<List<String>, PGobject> {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public PGobject convertToDatabaseColumn(List<String> attribute) {
    PGobject pg = new PGobject();
    pg.setType("jsonb");
    try {
      pg.setValue(objectMapper.writeValueAsString(attribute));
      return pg;
    } catch (JsonProcessingException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public List<String> convertToEntityAttribute(PGobject dbData) {
    try {
      return objectMapper.readerForListOf(String.class).readValue(dbData.getValue());
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
