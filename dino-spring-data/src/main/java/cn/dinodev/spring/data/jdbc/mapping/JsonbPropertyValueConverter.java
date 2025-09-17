// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.jdbc.mapping;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.convert.PropertyValueConverter;
import org.springframework.data.convert.ValueConversionContext;
import org.springframework.data.util.CastUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 * @date 2024-01-08 05:19:53
 */

@SuppressWarnings("rawtypes")
@Slf4j
public class JsonbPropertyValueConverter implements PropertyValueConverter {
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Object read(@NonNull Object value, @NonNull ValueConversionContext context) {
    PGobject sourceData = CastUtils.cast(value);

    var targetType = TypeDescriptor.valueOf(context.getProperty().getType());
    try {
      if (targetType.isCollection()) {
        return this.convertCollection(sourceData, targetType);

      } else if (targetType.isArray()) {
        return this.convertArray(sourceData, targetType);

      } else if (targetType.isMap()) {
        return this.convertMap(sourceData, targetType);

      } else {
        return this.convertObject(sourceData, targetType);

      }
    } catch (JsonProcessingException e) {
      log.error("convert error  to:{} value:{}", targetType, value, e);
      throw new ConversionFailedException(TypeDescriptor.forObject(value), targetType,
          value, e);
    }
  }

  @Override
  public Object write(@Nullable Object value, @NonNull ValueConversionContext context) {
    PGobject pg = new PGobject();
    pg.setType("jsonb");
    try {
      if (Objects.isNull(value)) {
        pg.setValue(null);
      }
      pg.setValue(this.objectMapper.writeValueAsString(value));
      return pg;
    } catch (SQLException | JsonProcessingException e) {
      log.error("convert write value:{}", value, e);
      throw new ConversionFailedException(TypeDescriptor.forObject(value), TypeDescriptor.valueOf(PGobject.class),
          value, e);
    }
  }

  private Object convertCollection(PGobject source, TypeDescriptor targetType) throws JsonProcessingException {
    if (source == null || source.getValue() == null || StringUtils.isBlank(source.getValue())) {
      return Collections.emptyList();
    }
    return this.objectMapper.readerForListOf(targetType.getResolvableType().getGeneric(0).getRawClass())
        .readValue(source.getValue());
  }

  private Object convertArray(PGobject source, TypeDescriptor targetType) throws JsonProcessingException {
    if (source == null || source.getValue() == null || StringUtils.isBlank(source.getValue())) {
      return null;
    }

    return this.objectMapper.readerForArrayOf(targetType.getResolvableType().getComponentType().getRawClass())
        .readValue(source.getValue());
  }

  private Object convertMap(PGobject source, TypeDescriptor targetType) throws JsonProcessingException {

    if (source == null || source.getValue() == null || StringUtils.isBlank(source.getValue())) {
      return Collections.emptyMap();
    }

    var mapValueTypeDescriptor = targetType.getMapValueTypeDescriptor();
    if (mapValueTypeDescriptor == null) {
      return Collections.emptyMap();
    }
    return this.objectMapper.readerForMapOf(mapValueTypeDescriptor.getResolvableType().getRawClass())
        .readValue(source.getValue());
  }

  private Object convertObject(PGobject source, TypeDescriptor targetType) throws JsonProcessingException {

    if (source == null || source.getValue() == null) {
      return null;
    }

    var cls = targetType.getResolvableType().getRawClass();
    if (cls == null) {
      cls = targetType.getType();
    }
    return this.objectMapper.readerFor(cls).readValue(source.getValue());
  }

}
