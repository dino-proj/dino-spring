/*
 *  Copyright 2021 dinodev.cn
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
