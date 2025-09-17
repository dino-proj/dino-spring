// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.dictionary;

import java.util.List;

import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;

/**
 *
 * @author Cody Lu
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
