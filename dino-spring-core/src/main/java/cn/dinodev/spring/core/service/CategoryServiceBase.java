// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.service;

import java.util.List;

import cn.dinodev.spring.core.modules.category.TreeNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.annotation.Nullable;

/**
 * @author JL
 * @Date: 2021/11/18
 */
public interface CategoryServiceBase<N extends TreeNode> {

  /**
   * 获取树结构
   * @param parentId
   * @param keyword
   * @return
   */
  List<N> findCategory(@Nullable Long parentId, @Nullable String keyword);

  /**
   * 分页获取分类树
   * @param parentId
   * @param keyword
   * @param page
   * @return
   */
  Page<N> findCategory(@Nullable Long parentId, @Nullable String keyword, Pageable page);
}
