package org.dinospring.core.service;

import java.io.Serializable;

import com.botbrain.dino.sql.builder.SelectSqlBuilder;

public interface SearchQuery extends Serializable {
  void buildSql(SelectSqlBuilder sql);
}
