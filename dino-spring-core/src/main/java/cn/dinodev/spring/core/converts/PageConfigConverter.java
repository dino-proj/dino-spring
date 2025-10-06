// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.converts;

import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.dinodev.spring.core.modules.framework.PageConfig;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

/**
 *
 * @author Cody Lu
 */

public class PageConfigConverter {

  /**
   * PageConfig写入转换器，将PageConfig对象转换为PostgreSQL的JSONB类型
   */
  @WritingConverter
  enum PageConfigWritingConverter implements Converter<PageConfig, PGobject> {
    /**
     * 单例实例
     */
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

  /**
   * PageConfig读取转换器，将PostgreSQL的JSONB类型转换为PageConfig对象
   */
  @ReadingConverter
  enum PageConfigReadingConverter implements Converter<PGobject, PageConfig> {
    /**
     * 单例实例
     */
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
