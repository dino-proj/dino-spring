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

package org.dinospring.core.modules.category;

import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.entity.Code;
import org.dinospring.core.service.CategoryServiceBase;
import org.dinospring.core.service.impl.ServiceBase;

import javax.annotation.Nullable;
import java.util.List;

/**
 *
 * @author tuuboo
 */
public abstract class CategoryService<E extends CategoryEntityBase> extends ServiceBase<E, Long> implements CategoryServiceBase<TreeNode> {


  @Override
  public List<TreeNode> findCategory(@Nullable Long parentId, @Nullable String keyword) {
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
