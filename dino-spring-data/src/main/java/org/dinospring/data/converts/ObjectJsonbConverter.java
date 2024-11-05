// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.converts;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 */

@Slf4j
@Component
public class ObjectJsonbConverter implements AttributeConverter<Object, PGobject> {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public PGobject convertToDatabaseColumn(Object attribute) {
    PGobject pg = new PGobject();
    pg.setType("jsonb");
    try {
      if (Objects.isNull(attribute)) {
        pg.setValue(null);
      }
      pg.setValue(objectMapper.writeValueAsString(attribute));
      return pg;
    } catch (IOException | SQLException e) {
      log.error("error parse json:{}", attribute, e);
      return null;
    }
  }

  @Override
  public Object convertToEntityAttribute(PGobject dbData) {
    if (StringUtils.isBlank(dbData.getValue())) {
      return null;
    }
    try {
      return objectMapper.readerFor(Object.class).readValue(dbData.getValue());
    } catch (JsonProcessingException e) {
      log.error("error Convert to Object json:{}", dbData.getValue(), e);
      throw new IllegalArgumentException(e);
    }
  }

}
