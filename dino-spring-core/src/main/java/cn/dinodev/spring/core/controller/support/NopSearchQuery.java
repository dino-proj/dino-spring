package cn.dinodev.spring.core.controller.support;

import cn.dinodev.spring.core.service.CustomQuery;
import cn.dinodev.sql.builder.SelectSqlBuilder;

/**
 *
 * @author Cody Lu
 */

public class NopSearchQuery implements CustomQuery {

  @Override
  public SelectSqlBuilder buildSql(SelectSqlBuilder sql) {
    return sql;
  }

}
