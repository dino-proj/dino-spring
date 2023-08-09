// Copyright 2021 dinodev.cn
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
 * @author Cody LU
 */

@Component
@Converter(autoApply = true)
public class ListLongJsonbConverter implements AttributeConverter<List<Long>, PGobject> {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public PGobject convertToDatabaseColumn(List<Long> attribute) {
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
  public List<Long> convertToEntityAttribute(PGobject dbData) {
    try {
      return objectMapper.readerForListOf(Long.class).readValue(dbData.getValue());
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
