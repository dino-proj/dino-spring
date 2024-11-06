// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.dinospring.auth.Operations;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.commons.property.PropertyView;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.commons.utils.ProjectionUtils;
import org.dinospring.core.service.CustomQuery;
import org.dinospring.core.service.Service;
import org.dinospring.core.vo.VoBase;
import org.dinospring.data.domain.EntityBase;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.annotation.Nonnull;

/**
 * @param S service类型
 * @param E entity类型
 * @param VO vo类型
 * @param SRC search query类型
 * @param REQ add和update的post body类型
 * @param K entity的主键类型
 *
 * @author Cody Lu
 * @author JL
 */

public interface CrudControllerBase<S extends Service<E, K>, E extends EntityBase<K>, VO extends VoBase<K>, SRC extends CustomQuery, REQ, K extends Serializable>
    extends ListControllerBase<S, E, VO, K, SRC> {

  /**
   * 对VO对象进行返回前的处理
   * @param vo
   * @return
   */
  default VO processVo(@Nonnull VO vo) {
    return vo;
  }

  /**
   * 对VoList里的VO对象进行返回前的处理
   * @param voList
   * @return
   */
  @Override
  default List<VO> processVoList(@Nonnull Collection<VO> voList) {
    return voList.stream().map(this::processVo).collect(Collectors.toList());
  }

  /**
   * 对查询请求对象进行预处理
   * @param req 请求对象
   * @param id Entity的ID，只有当更新时id才不为null，当是添加时id为null
   * @return
   */
  default REQ processReq(PostBody<REQ> req, K id) {
    return req.getBody();
  }

  /**
   * 根据ID查询详情
   * @param id Entity的ID
   * @return
   */
  @Operation(summary = "根据ID查询")
  @Parameter(in = ParameterIn.QUERY, name = "id", required = true)
  @GetMapping("id")
  @JsonView(PropertyView.Detail.class)
  @CheckPermission(Operations.READ)
  default Response<VO> getById(@RequestParam K id) {

    return Response.success(this.processVo(this.service().getById(id, this.voClass())));
  }

  /**
   * 新增Entity
   * @param req 对象字段
   * @return
   */
  @Operation(summary = "添加")
  @PostMapping("add")
  @Transactional(rollbackFor = Exception.class)
  @JsonView(PropertyView.Detail.class)
  @CheckPermission(Operations.CREATE)
  default Response<VO> add(
      @RequestBody @Validated(PropertyView.Insert.class) @JsonView(PropertyView.Insert.class) PostBody<REQ> req) {

    var body = this.processReq(req, null);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    VO vo = this.add(body);

    return Response.success(this.processVo(vo));
  }

  /**
   * 添加
   * @param req
   * @return
   */
  default VO add(REQ req) {
    E item = this.service().projection(this.entityClass(), req);

    item = this.service().save(item);

    return this.service().projection(this.voClass(), item);
  }

  /**
   * 更新Entity对象
   * @param id 要更新Entity的ID
   * @param req 对象字段
   * @return
   */
  @Operation(summary = "更新")
  @PostMapping("update")
  @Transactional(rollbackFor = Exception.class)
  @JsonView(PropertyView.Detail.class)
  @CheckPermission(Operations.UPDATE)
  default Response<VO> update(@RequestParam K id,
      @RequestBody @Validated(PropertyView.Update.class) @JsonView(PropertyView.Update.class) PostBody<REQ> req) {

    var body = this.processReq(req, id);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    VO vo = this.update(body, id);
    return Response.success(this.processVo(vo));
  }

  /**
   * 修改
   * @param req
   * @param id
   * @return
   */
  default VO update(REQ req, K id) {
    E item = this.service().getById(id);
    Assert.notNull(item, Status.CODE.FAIL_NOT_FOUND);

    ProjectionUtils.projectPropertiesWithView(req, item, PropertyView.Update.class);

    item = this.service().updateById(item);

    return this.service().projection(this.voClass(), item);
  }

  /**
   * 删除Entity对象
   * @param ids 要删除的Entity对象的ID
   * @return
   */
  @Operation(summary = "删除")
  @GetMapping("delete")
  @CheckPermission(Operations.DELETE)
  default Response<Boolean> dels(@RequestParam List<K> ids) {
    this.service().removeByIds(ids);
    return Response.success(true);
  }

  /**
   * 状态设置
   * @param ids 要设置状态的Entity对象的ID
   * @param status 要设置的状态
   * @return
   */
  @Operation(summary = "状态设置")
  @GetMapping("status")
  @Transactional(rollbackFor = Exception.class)
  @CheckPermission(Operations.EXECUTE)
  default Response<Boolean> status(@RequestParam List<K> ids, @RequestParam String status) {
    this.service().updateStatusByIds(ids, status);
    return Response.success(true);
  }

}
