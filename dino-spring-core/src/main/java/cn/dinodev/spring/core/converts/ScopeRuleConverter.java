// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.converts;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.dinodev.spring.core.modules.scope.ScopeRule;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 *
 * @author Cody Lu
 * @date 2022-03-31 17:27:22
 */

public class ScopeRuleConverter {
  @WritingConverter
  enum ScopeRuleWritingConverter implements Converter<ScopeRule, PGobject> {
    //ScopeRule WritingConverter
    INSTANCE;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PGobject convert(ScopeRule source) {
      PGobject pg = new PGobject();
      pg.setType("jsonb");
      try {
        pg.setValue(source.toJson(objectMapper));
        return pg;
      } catch (SQLException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }

}
