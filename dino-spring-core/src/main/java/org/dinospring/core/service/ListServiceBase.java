// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.dinospring.data.domain.LimitOffsetPageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Cody Lu
 * @date 2022-06-11 20:51:08
 */

public interface ListServiceBase<T, K extends Serializable> extends ServiceBase<T, K> {

  /**
   * 查询所有
   *
   * @return
   */
  default List<T> list() {
    return IterableUtils.toList(repository().findAll());
  }

  /**
   * 查询所有，并返回Iterable迭代器
   * @return
   */
  default Iterable<T> listIterable() {
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
    return IterableUtils.toList(repository().findAllById(idList));
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
