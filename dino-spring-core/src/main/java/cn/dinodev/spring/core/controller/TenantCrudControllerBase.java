// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import cn.dinodev.spring.auth.Operations;
import cn.dinodev.spring.auth.annotation.CheckPermission;
import cn.dinodev.spring.auth.annotation.Logic;
import cn.dinodev.spring.commons.property.PropertyView;
import cn.dinodev.spring.commons.request.PostBody;
import cn.dinodev.spring.commons.response.Response;
import cn.dinodev.spring.commons.response.Status;
import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.commons.utils.Assert;
import cn.dinodev.spring.commons.utils.ProjectionUtils;
import cn.dinodev.spring.core.annotion.param.ParamTenant;
import cn.dinodev.spring.core.service.CustomQuery;
import cn.dinodev.spring.core.service.Service;
import cn.dinodev.spring.core.vo.VoBase;
import cn.dinodev.spring.data.domain.EntityBase;
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

public interface TenantCrudControllerBase<S extends Service<E, K>, E extends EntityBase<K>, VO extends VoBase<K>, SRC extends CustomQuery, REQ, K extends Serializable>
    extends TenantListControllerBase<S, E, VO, K, SRC> {

  /**
   * 对VO对象进行返回前的处理
   * @param tenant 租户
   * @param vo
   * @return
   */
  default VO processVo(@Nonnull Tenant tenant, @Nonnull VO vo) {
    return vo;
  }

  /**
   * 对VoList里的VO对象进行返回前的处理
   * @param tenant 租户
   * @param voList
   * @return
   */
  @Override
  default List<VO> processVoList(@Nonnull Tenant tenant, @Nonnull Collection<VO> voList) {
    return voList.stream().map(t -> this.processVo(tenant, t)).collect(Collectors.toList());
  }

  /**
   * 对查询请求对象进行预处理
   * @param tenant 租户
   * @param req 请求对象
   * @param id Entity的ID，只有当更新时id才不为null，当是添加时id为null
   * @return
   */
  default REQ processReq(@Nonnull Tenant tenant, PostBody<REQ> req, K id) {
    return req.getBody();
  }

  /**
   * 根据ID查询详情
   * @param tenant 租户
   * @param id Entity的ID
   * @return
   */
  @Operation(summary = "根据ID查询")
  @ParamTenant
  @Parameter(in = ParameterIn.QUERY, name = "id", required = true)
  @GetMapping("id")
  @JsonView(PropertyView.Detail.class)
  @CheckPermission(Operations.READ)
  default Response<VO> getById(Tenant tenant, @RequestParam K id) {

    return Response.success(this.processVo(tenant, this.service().getById(id, this.voClass())));
  }

  /**
   * 新增Entity
   * @param tenant 租户
   * @param req 对象字段
   * @return
   */
  @Operation(summary = "添加")
  @ParamTenant
  @PostMapping("add")
  @Transactional(rollbackFor = Exception.class)
  @JsonView(PropertyView.Detail.class)
  @CheckPermission(Operations.CREATE)
  default Response<VO> add(Tenant tenant,
      @RequestBody @Validated(PropertyView.Insert.class) @JsonView(PropertyView.Insert.class) PostBody<REQ> req) {

    var body = this.processReq(tenant, req, null);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    VO vo = this.add(tenant, body);

    return Response.success(this.processVo(tenant, vo));
  }

  /**
   * 添加
   * @param tenant 租户
   * @param req
   * @return
   */
  default VO add(Tenant tenant, REQ req) {
    E item = this.service().projection(this.entityClass(), req);

    item = this.service().save(item);

    return this.service().projection(this.voClass(), item);
  }

  /**
   * 更新Entity对象
   * @param tenant 租户
   * @param id 要更新Entity的ID
   * @param req 对象字段
   * @return
   */
  @Operation(summary = "更新")
  @ParamTenant
  @PostMapping("update")
  @Transactional(rollbackFor = Exception.class)
  @JsonView(PropertyView.Detail.class)
  @CheckPermission(Operations.UPDATE)
  default Response<VO> update(Tenant tenant, @RequestParam K id,
      @RequestBody @Validated(PropertyView.Update.class) @JsonView(PropertyView.Update.class) PostBody<REQ> req) {

    var body = this.processReq(tenant, req, id);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    VO vo = this.update(tenant, body, id);
    return Response.success(this.processVo(tenant, vo));
  }

  /**
   * 修改
   * @param tenant 租户
   * @param req 请求对象
   * @param id Entity的ID
   * @return
   */
  default VO update(Tenant tenant, REQ req, K id) {
    E item = this.service().getById(id);
    Assert.notNull(item, Status.CODE.FAIL_NOT_FOUND);

    ProjectionUtils.projectPropertiesWithView(req, item, PropertyView.Update.class);

    item = this.service().updateById(item);

    return this.service().projection(this.voClass(), item);
  }

  /**
   * 删除Entity对象
   * @param tenant 租户
   * @param ids 要删除的Entity对象的ID
   * @return
   */
  @Operation(summary = "删除")
  @ParamTenant
  @GetMapping("delete")
  @CheckPermission(Operations.DELETE)
  default Response<Boolean> dels(Tenant tenant, @RequestParam List<K> ids) {
    this.delete(tenant, ids);
    return Response.success(true);
  }

  /**
   * 删除Entity对象
   * @param tenant 租户
   * @param ids 要删除的Entity对象的ID
   * @return
   */
  default void delete(Tenant tenant, List<K> ids) {
    this.service().removeByIds(ids);
  }

  /**
   * 状态设置
   * @param tenant 租户
   * @param ids 要设置状态的Entity对象的ID
   * @param status 要设置的状态
   * @return
   */
  @Operation(summary = "状态设置")
  @ParamTenant
  @GetMapping("status")
  @Transactional(rollbackFor = Exception.class)
  @CheckPermission(value = { Operations.EXECUTE, Operations.UPDATE }, logic = Logic.ANY)
  default Response<Boolean> status(Tenant tenant, @RequestParam List<K> ids, @RequestParam String status) {
    this.service().updateStatusByIds(ids, status);
    return Response.success(true);
  }

}
