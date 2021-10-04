// Copyright 2021 dinospring.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.dinospring.commons.request.PageReq;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.response.PageResponse;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.commons.utils.TypeUtils;
import org.dinospring.core.annotion.param.ParamPageable;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.service.SearchQuery;
import org.dinospring.core.service.Service;
import org.dinospring.core.vo.VoBase;
import org.dinospring.data.domain.EntityBase;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.CastUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

/**
 *
 * @author tuuboo
 */

public interface CrudControllerBase<S extends Service<E, K>, E extends EntityBase<K>, VO extends VoBase<K>, SRC extends SearchQuery, REQ, K extends Serializable> {

  /**
   * Service 服务实例
   * @return
   */
  S service();

  /**
   * Vo类的Class
   * @return
   */
  default Class<VO> voClass() {
    return TypeUtils.getGenericParamClass(this, CrudControllerBase.class, 2);
  }

  /**
   * Entity类的Class
   * @return
   */
  default Class<E> entityClass() {
    return TypeUtils.getGenericParamClass(this, CrudControllerBase.class, 1);
  }

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
  default List<VO> processVo(@Nonnull Collection<VO> voList) {
    voList.forEach(CrudControllerBase.this::processVo);
    if (List.class.isAssignableFrom(voList.getClass())) {
      return CastUtils.cast(voList);
    }
    return List.copyOf(voList);
  }

  /**
   * 对查询请求对象进行预处理
   * @param tenantId 租户ID
   * @param req 请求对象
   * @param id Entity的ID，只有当更新时id才不为null，当是添加时id为null
   * @return
   */
  default REQ processReq(String tenantId, PostBody<REQ> req, K id) {
    return req.getBody();
  }

  /**
   * 查询列表
   * @param tenantId 租户ID
   * @param pageReq 分页请求
   * @param req 查询条件
   * @return
   */
  @Operation(summary = "列表")
  @ParamTenant
  @ParamPageable
  @PostMapping("list")
  default PageResponse<VO> list(@PathVariable("tenant_id") String tenantId, PageReq pageReq,
      @RequestBody PostBody<SRC> req) {

    var pageData = service().listPage(pageReq.pageable());

    return PageResponse.success(pageData, t -> processVo(service().projection(voClass(), t)));
  }

  /**
   * 根据ID查询详情
   * @param tenantId 租户ID
   * @param id Entity的ID
   * @return
   */
  @Operation(summary = "根据ID查询")
  @ParamTenant
  @Parameter(in = ParameterIn.QUERY, name = "id", required = true)
  @GetMapping("id")
  default Response<VO> getByid(@PathVariable("tenant_id") String tenantId, @RequestParam K id) {

    return Response.success(processVo(service().getById(id, voClass())));
  }

  /**
   * 新增Entity
   * @param tenantId 租户ID
   * @param req 对象字段
   * @return
   */
  @Operation(summary = "添加")
  @ParamTenant
  @PostMapping("add")
  default Response<VO> add(@PathVariable("tenant_id") String tenantId, @RequestBody PostBody<REQ> req) {

    var body = processReq(tenantId, req, null);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    E item = service().projection(entityClass(), body);

    item = service().save(item);

    return Response.success(processVo(service().projection(voClass(), item)));
  }

  /**
   * 更新Entity对象
   * @param tenantId 租户ID
   * @param id 要更新Entity的ID
   * @param req 对象字段
   * @return
   */
  @Operation(summary = "更新")
  @ParamTenant
  @PostMapping("update")
  default Response<VO> update(@PathVariable("tenant_id") String tenantId, @RequestParam K id,
      @RequestBody PostBody<REQ> req) {

    var body = processReq(tenantId, req, id);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    E item = service().getById(id);
    Assert.notNull(item, Status.CODE.FAIL_NOT_FOUND);

    BeanUtils.copyProperties(body, item);

    item = service().updateById(item);
    return Response.success(processVo(service().projection(voClass(), item)));
  }

  /**
   * 删除Entity对象
   * @param tenantId 租户ID
   * @param ids 要删除的Entity对象的ID
   * @return
   */
  @Operation(summary = "删除")
  @ParamTenant
  @GetMapping("delete")
  default Response<Boolean> dels(@PathVariable("tenant_id") String tenantId, @RequestParam List<K> ids) {
    service().removeByIds(ids);
    return Response.success(true);
  }

}
