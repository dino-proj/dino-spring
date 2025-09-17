// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.service;

import java.io.Serializable;

import cn.dinodev.spring.data.dao.CrudRepositoryBase;

/**
 *
 * @author Cody Lu
 * @date 2022-06-11 20:52:35
 */

public interface ServiceBase<T, K extends Serializable> {
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
  default Class<T> getEntityClass() {
    return repository().entityClass();
  }
}
