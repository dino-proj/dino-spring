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
