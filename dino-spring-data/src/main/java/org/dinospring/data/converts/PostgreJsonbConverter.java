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

package org.dinospring.data.converts;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author tuuboo
 */

@Component
@ConditionalOnClass(PGobject.class)
public class PostgreJsonbConverter implements GenericConverter {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Set.of(new ConvertiblePair(PGobject.class, Collection.class), //
        new ConvertiblePair(PGobject.class, Object[].class), //
        new ConvertiblePair(PGobject.class, Map.class), //
        new ConvertiblePair(PGobject.class, String.class), //
        new ConvertiblePair(PGobject.class, Boolean.class), //
        new ConvertiblePair(PGobject.class, Long.class)//
    );
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    PGobject sourceData = CastUtils.cast(source);

    try {
      if (targetType.isCollection()) {
        if (sourceData == null || StringUtils.isBlank(sourceData.getValue())) {
          return Collections.emptyList();
        }
        return objectMapper.readerForListOf(targetType.getResolvableType().getGeneric(0).getRawClass())
            .readValue(sourceData.getValue());

      } else if (sourceData == null) {
        return null;
      } else if (targetType.isArray()) {
        return objectMapper.readerForArrayOf(targetType.getResolvableType().getComponentType().getRawClass())
            .readValue(sourceData.getValue());
      } else if (targetType.isMap()) {
        return objectMapper.readerForMapOf(targetType.getMapValueTypeDescriptor().getResolvableType().getRawClass())
            .readValue(sourceData.getValue());
      } else {
        return objectMapper.readerFor(targetType.getResolvableType().getRawClass()).readValue(sourceData.getValue());
      }
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
