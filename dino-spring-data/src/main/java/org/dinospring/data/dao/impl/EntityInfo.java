package org.dinospring.data.dao.impl;

import javax.persistence.Table;

import com.botbrain.dino.sql.dialect.Dialect;
import com.botbrain.dino.utils.NamingUtils;

import org.dinospring.data.annotion.TenantTable;
import org.springframework.core.annotation.AnnotationUtils;

import lombok.Data;

@Data
public class EntityInfo {

  private final TenantTable.TenantLevel tenantLevel;

  private final String tableName;

  private String quotedTableName;

  public boolean isTenantTable() {
    return tenantLevel == TenantTable.TenantLevel.TABLE;
  }

  public String getQuotedTableName() {
    return quotedTableName;
  }

  public static EntityInfo of(Dialect dialect, Class<?> cls) {
    var tenantAnno = AnnotationUtils.findAnnotation(cls, TenantTable.class);
    TenantTable.TenantLevel tenantLevel = tenantAnno == null ? TenantTable.TenantLevel.NOT : tenantAnno.level();

    String tableName;
    var tableAnno = AnnotationUtils.findAnnotation(cls, Table.class);
    if (tableAnno != null) {
      tableName = tableAnno.name();
    } else {
      tableName = NamingUtils.toSnake(cls.getSimpleName());
    }

    var ei = new EntityInfo(tenantLevel, tableName);
    ei.quotedTableName = dialect.quoteTableName(tableName);
    return ei;
  }
}
