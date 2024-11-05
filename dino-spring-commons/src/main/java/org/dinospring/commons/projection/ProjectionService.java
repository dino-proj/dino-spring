// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.projection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.dinospring.commons.utils.TypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 投影服务，用于将对象的属性投影到另一个对象中
 * @author Cody Lu
 * @date 2024-11-05 15:30:33
 */

@Service
@Slf4j
public class ProjectionService {
  @Autowired
  private ProjectionFactory projectionFactory;

  /**
   * 将source对象的属性投影到projectionType对象中
   * @param <P> source对象类型
   * @param <R> projectionType对象类型
   * @param projectionType 投影对象类型
   * @param source 源对象
   * @return 投影对象, 如果source为null则返回null
   */
  public <P, R> R projection(final Class<R> projectionType, P source) {
    if (Objects.isNull(source)) {
      return null;
    }
    if (projectionType.isInterface()) {
      return projectionFactory.createNullableProjection(projectionType, source);
    } else {
      try {
        R inst = TypeUtils.newInstance(projectionType);
        ProjectionUtils.projectProperties(source, inst);
        return inst;
      } catch (IllegalArgumentException | UnsupportedOperationException | SecurityException e) {
        log.error("create instance of {} error", projectionType.getName(), e);
        throw new IllegalArgumentException("instance of class:" + projectionType.getName() + " connot be created");
      }
    }
  }

  /**
   * 将source对象的属性投影到projectionType对象中
   * @param <P> source对象类型
   * @param <R> projectionType对象类型
   * @param projectionType 投影对象类型
   * @param source 源对象
   * @return 投影对象, 如果source为null则返回null
   */
  public <P, R> R projection(final Class<R> projectionType, Optional<P> source) {
    return projection(projectionType, source.orElse(null));
  }

  /**
   * 将sourceList中的对象的属性投影到projectionType对象中
   * @param <P> source对象类型
   * @param <R> projectionType对象类型
   * @param projectionType 投影对象类型
   * @param sourceList 源对象列表
   * @return 投影对象列表, 如果sourceList为null或空则返回空列表
   */
  public <P, R> List<R> projection(final Class<R> projectionType, Collection<P> sourceList) {
    if (CollectionUtils.isEmpty(sourceList)) {
      return Collections.emptyList();
    }
    return sourceList.stream().map(p -> projection(projectionType, p)).collect(Collectors.toList());
  }

  /**
   * 将sourceMap中的值的属性投影到projectionType对象中
   * @param <O> sourceMap的key类型
   * @param <P> sourceMap的value类型
   * @param <R> projectionType对象类型
   * @param projectionType 投影对象类型
   * @param sourceMap 源对象Map
   * @return 投影对象Map, 如果sourceMap为null或空则返回空Map
   */
  public <O, P, R> Map<O, R> projection(Class<R> projectionType, Map<O, P> sourceMap) {
    if (MapUtils.isEmpty(sourceMap)) {
      return Collections.emptyMap();
    }
    final Map<O, R> projMap = new HashMap<>(sourceMap.size());
    sourceMap.entrySet().forEach(e -> projMap.put(e.getKey(), projection(projectionType, e.getValue())));
    return projMap;
  }
}
