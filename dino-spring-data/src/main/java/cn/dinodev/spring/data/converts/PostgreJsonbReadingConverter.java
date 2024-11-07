// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.converts;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Component
@ConditionalOnClass(PGobject.class)
@Slf4j
@ReadingConverter
public class PostgreJsonbReadingConverter implements GenericConverter {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Set.of(new ConvertiblePair(PGobject.class, Collection.class), //
        new ConvertiblePair(PGobject.class, Object[].class), //
        new ConvertiblePair(PGobject.class, Map.class), //
        new ConvertiblePair(PGobject.class, Object.class), //
        new ConvertiblePair(PGobject.class, String.class), //
        new ConvertiblePair(PGobject.class, Boolean.class), //
        new ConvertiblePair(PGobject.class, Number.class)//
    );
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    PGobject sourceData = CastUtils.cast(source);

    try {
      if (targetType.isCollection()) {
        return convertCollection(sourceData, targetType);

      } else if (targetType.isArray()) {
        return convertArray(sourceData, targetType);

      } else if (targetType.isMap()) {
        return convertMap(sourceData, targetType);

      } else {
        return convertObject(sourceData, targetType);

      }
    } catch (JsonProcessingException e) {
      log.error("convert error from:{} to:{} value:{}", sourceType, targetType, source, e);
      throw new ConversionFailedException(sourceType, targetType, source, e);
    }
  }

  private Object convertCollection(PGobject source, TypeDescriptor targetType) throws JsonProcessingException {
    if (source == null || source.getValue() == null || StringUtils.isBlank(source.getValue())) {
      return Collections.emptyList();
    }
    return objectMapper.readerForListOf(targetType.getResolvableType().getGeneric(0).getRawClass())
        .readValue(source.getValue());
  }

  private Object convertArray(PGobject source, TypeDescriptor targetType) throws JsonProcessingException {
    if (source == null || source.getValue() == null || StringUtils.isBlank(source.getValue())) {
      return null;
    }

    return objectMapper.readerForArrayOf(targetType.getResolvableType().getComponentType().getRawClass())
        .readValue(source.getValue());
  }

  private Object convertMap(PGobject source, TypeDescriptor targetType) throws JsonProcessingException {

    if (source == null || source.getValue() == null || StringUtils.isBlank(source.getValue())) {
      return Collections.emptyMap();
    }

    return objectMapper.readerForMapOf(targetType.getMapValueTypeDescriptor().getResolvableType().getRawClass())
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
    return objectMapper.readerFor(cls).readValue(source.getValue());
  }

}
