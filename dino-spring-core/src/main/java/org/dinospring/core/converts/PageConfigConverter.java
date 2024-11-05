// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.converts;

import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dinospring.core.modules.framework.PageConfig;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

/**
 *
 * @author Cody LU
 */

public class PageConfigConverter {

  @WritingConverter
  enum PageConfigWritingConverter implements Converter<PageConfig, PGobject> {
    //PageConfig WritingConverter
    INSTANCE;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PGobject convert(PageConfig source) {
      PGobject pg = new PGobject();
      pg.setType("jsonb");
      try {
        pg.setValue(objectMapper.writeValueAsString(source));
        return pg;
      } catch (JsonProcessingException | SQLException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }

  @ReadingConverter
  enum PageConfigReadingConverter implements Converter<PGobject, PageConfig> {
    //PageConfig ReadingConverter
    INSTANCE;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PageConfig convert(PGobject pg) {
      try {
        return objectMapper.readValue(pg.getValue(), PageConfig.class);
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }
}
