package org.dinospring.core.controller.support;

import com.botbrain.dino.sql.builder.SelectSqlBuilder;

import org.dinospring.core.service.CustomQuery;

/**
 *
 * @author tuuboo
 */

public class NopSearchQuery implements CustomQuery {

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    return sql;
  }

}
