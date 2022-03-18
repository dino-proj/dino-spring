package org.dinospring.core.controller.support;

import org.dinospring.core.service.CustomQuery;
import org.dinospring.data.sql.builder.SelectSqlBuilder;

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
