// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.controller;

import java.io.Serializable;
import java.util.Collection;

import org.dinospring.auth.Operations;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.commons.property.PropertyView;
import org.dinospring.commons.request.PageReq;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.request.SortReq;
import org.dinospring.commons.response.PageResponse;
import org.dinospring.core.annotion.param.ParamPageable;
import org.dinospring.core.annotion.param.ParamSort;
import org.dinospring.core.service.CustomQuery;
import org.dinospring.core.service.ListServiceBase;
import org.dinospring.core.vo.VoBase;
import org.dinospring.data.domain.EntityBase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nonnull;

/**
 *
 * @author Cody Lu
 * @date 2022-06-11 21:04:00
 */

public interface ListControllerBase<S extends ListServiceBase<E, K>, E extends EntityBase<K>, VO extends VoBase<K>, K extends Serializable, SRC extends CustomQuery>
    extends ControllerBase<S, E, VO, K> {

  /**
   * 对VoList里的VO对象进行返回前的处理
   * @param voList
   * @return
   */
  default Collection<VO> processVoList(@Nonnull Collection<VO> voList) {
    return voList;
  }

  /**
   * 查询列表
   * @param pageReq 分页请求
   * @param sortReq 排序
   * @param req 查询条件
   * @return
   */
  @Operation(summary = "列表")
  @ParamPageable
  @ParamSort
  @PostMapping("list")
  @JsonView(PropertyView.Summary.class)
  @CheckPermission(Operations.LIST)
  default PageResponse<VO> list(PageReq pageReq, SortReq sortReq, @RequestBody PostBody<SRC> req) {
    var pageable = pageReq.pageable(sortReq, "t.");

    var query = req.getBody();
    var pageData = query == null ? this.service().listPage(pageable, this.voClass())
        : this.service().listPage(query, pageable, this.voClass());

    return PageResponse.success(pageData, this::processVoList);
  }

}
