// Copyright 2021 dinospring.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.data.dao.impl;

import javax.persistence.Table;

import com.botbrain.dino.sql.dialect.Dialect;
import com.botbrain.dino.utils.NamingUtils;

import org.dinospring.data.annotion.TenantTable;
import org.dinospring.data.domain.LogicalDelete;
import org.springframework.core.annotation.AnnotationUtils;

import lombok.Data;

/**
 *
 * @author tuuboo
 */

@Data
public class EntityInfo {

  private final TenantTable.TenantLevel tenantLevel;

  private final String tableName;
  
  private final boolean logicalDelete;

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

    var logicalDelete = LogicalDelete.class.isAssignableFrom(cls);

    var ei = new EntityInfo(tenantLevel, tableName,logicalDelete);
    ei.quotedTableName = dialect.quoteTableName(tableName);
    return ei;
  }
}
