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
 * @author tuuboo
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
