// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.data.domain.LogicalDelete;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础服务Service
 *
 * @author Cody Lu
 */
public interface Service<T, K extends Serializable> extends ListServiceBase<T, K> {

  /**
   * 默认批次提交数量
   */
  int DEFAULT_BATCH_SIZE = 1000;

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
   * 状态设置
   * @param ids
   * @param status
   * @return 更新记录数
   */
  @Transactional(rollbackFor = Exception.class)
  default long updateStatusByIds(Collection<K> ids, String status) {
    return repository().updateStatusByIds(ids, status);
  }

  /**
   * 状态设置
   * @param id
   * @param status
   * @return 更新记录数
   */
  @Transactional(rollbackFor = Exception.class)
  default long updateStatusById(K id, String status) {
    return repository().updateStatusById(id, status);
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
    return repository().countOk();
  }

  /**
   * 根据 自定义查询条件，查询总记录数
   *
   * @param query 查询条件组装
   * @return
   */
  default long count(CustomQuery query) {
    var sql = repository().newSelect("t").column("count(1)");
    query.buildSql(sql);
    return repository().getOne(sql, Long.class);
  }

  /**
   * 检查是否存在记录
   * @param id 主键ID
   * @return 是否存在
   */
  default boolean exists(K id) {
    return repository().existsById(id);
  }

  /**
   * 根据条件检查是否存在记录
   * @param search 查询条件
   * @return 是否存在
   */
  default boolean exists(CustomQuery search) {
    var sql = repository().newSelect("t").column("t.id");
    search.buildSql(sql);
    sql.limit(1);

    return Objects.nonNull(repository().getOne(sql, repository().keyClass()));
  }

}
