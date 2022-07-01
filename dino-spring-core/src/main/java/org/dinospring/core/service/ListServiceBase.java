// Copyright 2022 dinospring.cn
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

package org.dinospring.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.dinospring.data.domain.LimitOffsetPageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author tuuboo
 * @date 2022-06-11 20:51:08
 */

public interface ListServiceBase<T, K extends Serializable> extends ServiceBase<T, K> {

  /**
   * 查询所有
   *
   * @return
   */
  default List<T> list() {
    return repository().findAll();
  }

  /**
   * 获取指定数量的entity记录
   * @param limit 条数
   * @param offset 从0开始
   * @return
   */
  default List<T> listLimit(int limit, int offset) {
    return repository().findAll(new LimitOffsetPageable(offset, limit)).getContent();
  }

  /**
   * 获取指定数量的entity记录
   * @param limit
   * @return
   */
  default List<T> listLimit(int limit) {
    return listLimit(limit, 0);
  }

  /**
   * 查询（根据ID 批量查询）
   *
   * @param idList 主键ID列表
   * @return
   */
  default List<T> listByIds(Collection<K> idList) {
    return repository().findAllById(idList);
  }

  /**
   * 分页查询
   * @param page 分页信息
   * @return
   */
  default Page<T> listPage(Pageable page) {
    return repository().findAll(page);
  }

  /**
   * 分页查询
   * @param page 分页信息
   * @param cls 类型
   * @return
   */
  default <C> Page<C> listPage(Pageable page, Class<C> cls) {
    var sql = repository().newSelect();
    return repository().queryPage(sql, page, cls);
  }

  /**
   * 分页查询
   * @param search 查询条件
   * @param page 分页信息
   * @return
   */
  default Page<T> listPage(CustomQuery search, Pageable page) {
    var sql = repository().newSelect("t").column("t.*");
    search.buildSql(sql);

    return repository().queryPage(sql, page);
  }

  /**
   * 分页查询
   * @param search 查询条件
   * @param page 分页信息
   * @param cls 类型
   * @return
   */
  default <C> Page<C> listPage(CustomQuery search, Pageable page, Class<C> cls) {
    var sql = repository().newSelect("t").column("t.*");
    search.buildSql(sql);

    return repository().queryPage(sql, page, cls);
  }

}
