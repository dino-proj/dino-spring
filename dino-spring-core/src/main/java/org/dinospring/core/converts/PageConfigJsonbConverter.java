package org.dinospring.core.converts;

import java.sql.SQLException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dinospring.core.modules.framework.PageConfig;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public class PageConfigJsonbConverter implements AttributeConverter<PageConfig, PGobject> {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public PGobject convertToDatabaseColumn(PageConfig attribute) {
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
  public PageConfig convertToEntityAttribute(PGobject dbData) {
    try {
      return objectMapper.readValue(dbData.getValue(), PageConfig.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
