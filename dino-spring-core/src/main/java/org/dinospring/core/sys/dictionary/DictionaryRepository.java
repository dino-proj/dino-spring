// Copyright 2021 dinodev.cn
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

package org.dinospring.core.sys.dictionary;

import java.util.List;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;

/**
 *
 * @author Cody LU
 */

public interface DictionaryRepository extends CrudRepositoryBase<DictionaryEntity, Long> {

  /**
   * 查询字典项列表
   * @param key
   * @return
   */
  @Query("FROM DictItemEntity where key=:key ORDER BY orderNum")
  List<DictItemEntity> listDictItems(String key);

  /**
   * 查询字典项
   * @param key
   * @param code
   * @return
   */
  @Query("FROM DictItemEntity where key=:key and code=:code")
  DictItemEntity getDictItem(String key, String code);

  /**
   * 删除字典项
   * @param key
   * @param code
   * @return
   */
  @Modifying
  @Query("DELETE FROM DictItemEntity where key=:key and code=:code")
  long deleteDictItem(String key, String code);

  /**
   * 清楚字典项所有数据
   * @param key
   * @return
   */
  @Modifying
  @Query("DELETE FROM DictItemEntity where key=:key")
  long deleteDictItems(String key);
}
