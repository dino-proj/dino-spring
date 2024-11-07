// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.converts;

import java.sql.SQLException;

import cn.dinodev.spring.core.modules.framework.PageConfig;
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
public class PageConfigJsonbConverter implements AttributeConverter<PageConfig, PGobject> {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public PGobject convertToDatabaseColumn(PageConfig attribute) {
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
  public PageConfig convertToEntityAttribute(PGobject dbData) {
    try {
      return objectMapper.readValue(dbData.getValue(), PageConfig.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
