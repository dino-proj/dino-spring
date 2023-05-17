// Copyright 2022 dinodev.cn
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

import com.fasterxml.jackson.databind.ObjectMapper;

import org.dinospring.core.modules.scope.ScopeRule;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 *
 * @author tuuboo
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
