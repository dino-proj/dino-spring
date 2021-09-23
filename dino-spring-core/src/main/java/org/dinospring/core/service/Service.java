
package org.dinospring.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.data.dao.CURDRepositoryBase;
import org.dinospring.data.domain.LimitOffsetPageable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础服务Service
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
    CURDRepositoryBase<T, K> repository();

    /**
     * 获取 entity 的 class
     *
     * @return {@link Class<T>}
     */
    Class<T> getEntityClass();

    <P, R> R projection(Class<R> cls, P p);

    <P, R> R projection(Class<R> cls, Optional<P> p);

    <P, R> List<R> projection(Class<R> cls, Collection<P> p);

    <O, P, R> Map<O, R> projection(Class<R> cls, Map<O, P> p);

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Transactional(rollbackFor = Exception.class)
    default <S extends T> S save(S entity) {
        return repository().save(entity);
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
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
     */
    @Transactional(rollbackFor = Exception.class)
    boolean saveBatch(Collection<T> entityList, int batchSize);

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
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
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return saveBatch(entityList, batchSize);
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
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
     */
    default void removeByIds(Collection<K> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        repository().deleteAllById(idList);
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    default <S extends T> S updateById(S entity) {
        return repository().save(entity);
    }

    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
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
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return saveBatch(entityList, batchSize);
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     */
    @Transactional(rollbackFor = Exception.class)
    default <S extends T> S saveOrUpdate(S entity) {
        return save(entity);
    }

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    default T getById(K id) {
        return repository().findById(id).orElse(null);
    }

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    default <O> O getById(K id, Class<O> clazz) {
        return this.projection(clazz, repository().findById(id).orElse(null));
    }

    /**
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
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
     * @param queryWrapper
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
     */
    default List<T> listByIds(Collection<K> idList) {
        return repository().findAllById(idList);
    }

    default Page<T> listPage(Pageable page) {
        return repository().findAll(page);
    }

    default Page<T> listPage(SearchQuery search, Pageable page) {
        var sql = repository().newSelect("t");
        search.buildSql(sql);

        return repository().queryPage(sql, page);
    }

    /**
     * 查询总记录数
     *
     * @see Wrappers#emptyWrapper()
     */
    default long count() {
        return repository().count();
    }

    /**
     * 根据 Example，查询总记录数
     *
     */
    default <S extends T> long count(Example<S> example) {
        return repository().count(example);
    }

}
