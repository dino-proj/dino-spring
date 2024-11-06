// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.category;

import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.entity.Code;
import org.dinospring.core.service.CategoryServiceBase;
import org.dinospring.core.service.impl.ServiceBase;

import jakarta.annotation.Nullable;
import java.util.List;

/**
 *
 * @author Cody Lu
 * @author JL
 */
public abstract class CategoryService<E extends CategoryEntityBase, N extends TreeNode> extends ServiceBase<E, Long>
    implements CategoryServiceBase<N> {

  @Override
  public List<N> findCategory(@Nullable Long parentId, @Nullable String keyword) {
    if (parentId == null) {
      parentId = 0L;
    }
    var sql = repository().newSelect("t1");
    sql.leftJoin(repository().tableName(), "t2", "t1.id = t2.parent_id and t2.status='ok'");
    sql.columns("t1.id as value, t1.name as label, t1.icon, count(t2.id) as child_count");
    sql.eqIfNotNull("t1.parent_id", parentId);
    sql.like("t1.name", keyword);
    sql.eq("t1.status", Code.STATUS.OK.name().toLowerCase());
    sql.groupBy("t1.id");
    return repository().queryList(sql, TypeUtils.getGenericSuperclassParamClass(this, CategoryService.class, 1));
  }

}
