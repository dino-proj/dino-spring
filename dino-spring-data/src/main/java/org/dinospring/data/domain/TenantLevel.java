package org.dinospring.data.domain;

/**
 * @author Cody Lu
 */

public enum TenantLevel {
  //SCHEMA级别
  SCHEMA,
  //TABLE级别，表名为 [tableName][sep][tanentId]
  TABLE,
  //ROW级别，字段名为"tenent_id"
  ROW,
  //非Tenant表
  NOT
}