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
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody LU
 */

@Component
@ConditionalOnClass(PGobject.class)
@Slf4j
@WritingConverter
public class PostgreJsonbWritingConverter implements GenericConverter {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Set.of(new ConvertiblePair(Collection.class, PGobject.class), //
        new ConvertiblePair(Map.class, PGobject.class), //
        new ConvertiblePair(Object[].class, PGobject.class), //
        new ConvertiblePair(String[].class, PGobject.class), //
        new ConvertiblePair(Boolean[].class, PGobject.class), //
        new ConvertiblePair(Number[].class, PGobject.class)//
    );
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    PGobject pg = new PGobject();
    pg.setType("jsonb");
    try {
      if (Objects.isNull(source)) {
        pg.setValue(null);
      }
      pg.setValue(objectMapper.writeValueAsString(source));
      return pg;
    } catch (SQLException | JsonProcessingException e) {
      log.error("convert error from:{} to:{} value:{}", sourceType, targetType, source, e);
      throw new ConversionFailedException(sourceType, targetType, source, e);
    }
  }

}
