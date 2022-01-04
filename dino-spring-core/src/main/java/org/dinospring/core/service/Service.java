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

package org.dinospring.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.dinospring.data.domain.LimitOffsetPageable;
import org.dinospring.data.domain.LogicalDelete;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础服务Service
 *
 * @author tuuboo
 */
public interface Service<T, K extends Serializable> {

  /**
   * 默认批次提交数量
   */
  int DEFAULT_BATCH_SIZE = 1000;

  /**
   * 获取对应 entity 的 BaseMapper
   *
   * @return BaseMapper
   */
  CrudRepositoryBase<T, K> repository();

  /**
   * 获取 entity 的 class
   *
   * @return {@link Class<T>}
   */
  Class<T> getEntityClass();

  /**
   * 对object进行投影或者属性拷贝
   * @param <P> 源对象类型
   * @param <R> 目标对象类型
   * @param targetCls 目标对象Class
   * @param source
   * @return
   */
  <P, R> R projection(Class<R> targetCls, P source);

  /**
   * 对object进行投影或者属性拷贝
   * @param <P> 源对象类型
   * @param <R> 目标对象类型
   * @param targetCls 目标对象Class
   * @param source 源对象
   * @return
   */
  <P, R> R projection(Class<R> targetCls, Optional<P> source);

  /**
   * 对object集合进行投影或者属性拷贝
   * @param <P> 源对象类型
   * @param <R> 目标对象类型
   * @param targetCls 目标对象Class
   * @param source 源对象
   * @return
   */
  <P, R> List<R> projection(Class<R> targetCls, Collection<P> source);

  /**
   * 对Map进行投影或者属性拷贝
   * @param <O>
   * @param <P> 源对象类型
   * @param <R> 目标对象类型
   * @param targetCls 目标对象Class
   * @param source 源对象
   * @return
   */
  <O, P, R> Map<O, R> projection(Class<R> targetCls, Map<O, P> source);

  /**
   * 插入一条记录（选择字段，策略插入）
   *
   * @param entity 实体对象
   * @return 返回保存的对象
   */
  @Transactional(rollbackFor = Exception.class)
  default <S extends T> S save(S entity) {
    return repository().save(entity);
  }

  /**
   * 插入（批量）
   *
   * @param entityList 实体对象集合
   * @return 是否插入成功
   */
  @Transactional(rollbackFor = Exception.class)
  default boolean saveBatch(Collection<T> entityList) {
    return saveBatch(entityList, DEFAULT_BATCH_SIZE);
  }

  /**
   * 插入（批量）
   *
   * @param entityList 实体对象集合
   * @param batchSize  插入批次数量
   * @return 是否插入成功
   */
  @Transactional(rollbackFor = Exception.class)
  boolean saveBatch(Collection<T> entityList, int batchSize);

  /**
   * 批量修改插入
   *
   * @param entityList 实体对象集合
   * @return 是否插入成功
   */
  @Transactional(rollbackFor = Exception.class)
  default boolean saveOrUpdateBatch(Collection<T> entityList) {
    return saveOrUpdateBatch(entityList, DEFAULT_BATCH_SIZE);
  }

  /**
   * 批量修改插入
   *
   * @param entityList 实体对象集合
   * @param batchSize  每次的数量
   * @return 是否插入成功
   */
  @Transactional(rollbackFor = Exception.class)
  default boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
    return saveBatch(entityList, batchSize);
  }

  /**
   * 根据 ID 删除
   *
   * @param id 主键ID
   * @return 是否删除成功
   */
  @Transactional(rollbackFor = Exception.class)
  default boolean removeById(K id) {
    repository().deleteById(id);
    return true;
  }

  /**
   * 删除（根据ID 批量删除）
   *
   * @param idList 主键ID列表
   * @return 是否删除成功
   */
  default void removeByIds(Collection<K> idList) {
    if (CollectionUtils.isEmpty(idList)) {
      return;
    }

    boolean isDelete = false;
    Class<T> entityClass = getEntityClass();
    Class<?>[] interfaces = entityClass.getInterfaces();
    for (Class<?> anInterface : interfaces) {
      if (anInterface == LogicalDelete.class) {
        isDelete = true;
        break;
      }
    }

    if (!isDelete) {
      repository().deleteAllById(idList);
    } else {
      repository().updateStatusByIds(idList, "deleted");
    }

  }

  /**
   * 根据 ID 选择修改
   *
   * @param entity 实体对象
   * @return 保存的实体对象
   */
  default <S extends T> S updateById(S entity) {
    return repository().save(entity);
  }

  /**
   * 根据ID 批量更新
   *
   * @param entityList 实体对象集合
   * @return 保存的实体对象
   */
  @Transactional(rollbackFor = Exception.class)
  default boolean updateBatchById(Collection<T> entityList) {
    return updateBatchById(entityList, DEFAULT_BATCH_SIZE);
  }

  /**
   * 根据ID 批量更新
   *
   * @param entityList 实体对象集合
   * @param batchSize  更新批次数量
   * @return 是否保存成功
   */
  @Transactional(rollbackFor = Exception.class)
  default boolean updateBatchById(Collection<T> entityList, int batchSize) {
    return saveBatch(entityList, batchSize);
  }

  /**
   * 保存或更新Entity
   *
   * @param entity 实体对象
   * @return 保存的实体对象
   */
  @Transactional(rollbackFor = Exception.class)
  default <S extends T> S saveOrUpdate(S entity) {
    return save(entity);
  }

  /**
   * 根据 ID 查询
   *
   * @param id 主键ID
   * @return Entity对象，如果查询不到返回null
   */
  default T getById(K id) {
    return repository().findById(id).orElse(null);
  }

  /**
   * 根据 ID 查询
   *
   * @param id 主键ID
   * @param clazz 查询的映射
   * @return Entity对象，如果查询不到返回null
   */
  default <O> O getById(K id, Class<O> clazz) {
    return this.projection(clazz, repository().findById(id).orElse(null));
  }

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
   * @param queryWrapper
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
   * @param search 查询条件
   * @param page 分页信息
   * @return
   */
  default Page<T> listPage(CustomQuery search, Pageable page) {
    var sql = repository().newSelect("t");
    search.buildSql(sql);

    return repository().queryPage(sql, page);
  }

  /**
   * 查询总记录数
   *
   * @return
   */
  default long count() {
    return repository().count();
  }

  /**
  * 查询状态为有效的记录数
  *
  * @return
  */
  default long countOk() {
    return repository().countOk().orElse(0L);
  }

  /**
   * 根据 Example，查询总记录数
   *
   * @param example 样例
   * @return
   */
  default <S extends T> long count(Example<S> example) {
    return repository().count(example);
  }

}
