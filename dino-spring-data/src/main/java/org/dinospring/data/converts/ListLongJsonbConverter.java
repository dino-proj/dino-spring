package org.dinospring.data.converts;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
