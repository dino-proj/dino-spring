// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.service.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import cn.dinodev.spring.commons.context.ContextHelper;
import cn.dinodev.spring.commons.sys.User;
import cn.dinodev.spring.commons.utils.BatchUtils;
import cn.dinodev.spring.commons.utils.ProjectionUtils;
import cn.dinodev.spring.commons.utils.TypeUtils;
import cn.dinodev.spring.core.service.Service;
import cn.dinodev.spring.data.domain.EntityBase;
import cn.dinodev.spring.data.domain.TenantRowEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 */

@Slf4j
public abstract class ServiceBase<T, K extends Serializable> implements Service<T, K> {

  @Autowired
  private ProjectionFactory projectionFactory;

  @Override
  public <P, R> R projection(final Class<R> cls, P obj) {
    if (Objects.isNull(obj)) {
      return null;
    }
    if (cls.isInterface()) {
      return projectionFactory.createNullableProjection(cls, obj);
    } else {
      try {
        R inst = cls.getDeclaredConstructor().newInstance();
        ProjectionUtils.projectProperties(obj, inst);
        return inst;
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
          | NoSuchMethodException | SecurityException e) {
        log.error("create instance of {} error", cls.getName(), e);
        throw new IllegalArgumentException("instance of class:" + cls.getName() + " connot be created");
      }
    }
  }

  @Override
  public <P, R> R projection(final Class<R> cls, Optional<P> p) {
    return projection(cls, p.orElse(null));
  }

  @Override
  public <P, R> List<R> projection(final Class<R> cls, Collection<P> list) {
    if (CollectionUtils.isEmpty(list)) {
      return Collections.emptyList();
    }
    return list.stream().map(p -> projection(cls, p)).collect(Collectors.toList());
  }

  @Override
  public <O, P, R> Map<O, R> projection(Class<R> cls, Map<O, P> map) {
    if (MapUtils.isEmpty(map)) {
      return Collections.emptyMap();
    }
    final Map<O, R> projMap = new HashMap<>(map.size());
    map.entrySet().forEach(e -> projMap.put(e.getKey(), projection(cls, e.getValue())));
    return projMap;
  }

  /**
   * 创建一个新的Entity
   */
  protected T newEntity() {
    return TypeUtils.newInstance(getEntityClass());
  }

  /**
   * 在创建值之前调用
   * @param entity 实体对象
   */
  protected void beforeSaveEntity(T entity) {
    if (entity instanceof EntityBase) {
      var now = new Date();
      var base = (EntityBase<?>) entity;
      if (Objects.isNull(base.getCreateAt())) {
        base.setCreateAt(now);
      }
      base.setUpdateAt(now);

      String createBy = ((EntityBase<?>) entity).getCreateBy();
      if (StringUtils.isBlank(createBy)) {
        User<Serializable> user = ContextHelper.currentUser();
        if (user != null) {
          ((EntityBase<?>) entity).setCreateBy(String.format("%s:%s", user.getId(), user.getUserType().getType()));
        }
      }
    }

    if (entity instanceof TenantRowEntity && StringUtils.isNotEmpty(ContextHelper.currentTenantId())) {
      var base = (TenantRowEntity) entity;
      if (StringUtils.isEmpty(base.getTenantId())) {
        base.setTenantId(ContextHelper.currentTenantId());
      }
    }
  }

  /**
   * 用于创建之前的自动填充等场景调用
   */
  private <S extends T> void beforeSaveEntities(Collection<S> entityList) {
    if (CollectionUtils.isEmpty(entityList)) {
      return;
    }
    for (S entity : entityList) {
      beforeSaveEntity(entity);
    }
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public <S extends T> S save(S entity) {
    if (entity == null) {
      log.warn("null entity cannot be saved");
      return null;
    }
    beforeSaveEntity(entity);
    return repository().save(entity);
  }

  /**
   * 批量插入
   *
   * @param entityList ignore
   * @param batchSize  ignore
   * @return ignore
   */
  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean saveBatch(@Nonnull Collection<T> entityList, int batchSize) {
    // 批量插入
    var saveCount = new AtomicInteger(0);
    BatchUtils.executeBatch(entityList, batchSize, b -> {
      beforeSaveEntities(b);
      saveCount.addAndGet(IterableUtils.size(repository().saveAll(b)));
    });
    return saveCount.get() == entityList.size();
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public <S extends T> S updateById(S entity) {
    beforeUpdateEntity(entity);
    return Service.super.updateById(entity);
  }

  protected void beforeUpdateEntity(T entity) {
    if (entity instanceof EntityBase) {
      ((EntityBase<?>) entity).setUpdateAt(new Date());
    }
  }
}
