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

package org.dinospring.core.sys.dictionary;

import java.util.List;

import org.dinospring.core.sys.dictionary.DictionaryEntity.DictItem;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author tuuboo
 */

public interface DictionaryRepository extends CrudRepositoryBase<DictionaryEntity, Long> {

  /**
   * 查询字典项
   * @param key
   * @return
   */
  @Query("FROM DictItem where key=:key ORDER BY orderNum")
  List<DictItem> listDictItems(String key);
}
