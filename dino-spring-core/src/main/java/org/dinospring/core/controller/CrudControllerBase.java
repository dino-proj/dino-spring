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

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.apache.commons.collections4.CollectionUtils;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.commons.request.PageReq;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.request.SortReq;
import org.dinospring.commons.response.PageResponse;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.annotion.param.ParamPageable;
import org.dinospring.core.annotion.param.ParamSort;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.service.CustomQuery;
import org.dinospring.core.service.Service;
import org.dinospring.core.vo.VoBase;
import org.dinospring.data.domain.EntityBase;
import org.dinospring.data.json.PropertyView;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.CastUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @param S service类型
 * @param E entity类型
 * @param VO vo类型
 * @param SRC search query类型
 * @param REQ add和update的post body类型
 * @param K entity的主键类型
 *
 * @author tuuboo
 * @author JL
 */

public interface CrudControllerBase<S extends Service<E, K>, E extends EntityBase<K>, VO extends VoBase<K>, SRC extends CustomQuery, REQ, K extends Serializable>
  extends ControllerBase<S, E, VO, K> {

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
  default List<VO> processVoList(@Nonnull Collection<VO> voList) {
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
   * @param sortReq 排序
   * @param req 查询条件
   * @return
   */
  @Operation(summary = "列表")
  @ParamTenant
  @ParamPageable
  @ParamSort
  @PostMapping("list")
  @JsonView(PropertyView.OnSummary.class)
  @CheckPermission("list | supper")
  default PageResponse<VO> list(@PathVariable("tenant_id") String tenantId, PageReq pageReq, SortReq sortReq,
                                @RequestBody PostBody<SRC> req) {

    List<String> sort = sortReq.getSort();
    if (CollectionUtils.isNotEmpty(sort)) {
      sort = sort.stream().map(s -> "t." + s).collect(Collectors.toList());
      sortReq.setSort(sort);
    }
    var pageable = pageReq.pageable(sortReq);

    var query = req.getBody();
    var pageData = query == null ? service().listPage(pageable, voClass())
      : service().listPage(query, pageable, voClass());

    return PageResponse.success(pageData, this::processVoList);
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
  @JsonView(PropertyView.OnDetail.class)
  @CheckPermission("detail | supper")
  default Response<VO> getById(@PathVariable("tenant_id") String tenantId, @RequestParam K id) {

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
  @Transactional(rollbackFor = Exception.class)
  @CheckPermission("add | supper")
  default Response<VO> add(@PathVariable("tenant_id") String tenantId, @RequestBody PostBody<REQ> req) {

    var body = processReq(tenantId, req, null);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    VO vo = add(body);

    return Response.success(processVo(vo));
  }

  /**
   * 添加
   * @param req
   * @return
   */
  default VO add(REQ req) {
    E item = service().projection(entityClass(), req);

    item = service().save(item);

    return service().projection(voClass(), item);
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
  @Transactional(rollbackFor = Exception.class)
  @CheckPermission("update | supper")
  default Response<VO> update(@PathVariable("tenant_id") String tenantId, @RequestParam K id,
                              @RequestBody PostBody<REQ> req) {

    var body = processReq(tenantId, req, id);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    VO vo = update(body, id);
    return Response.success(processVo(vo));
  }

  /**
   * 修改
   * @param req
   * @param id
   * @return
   */
  default VO update(REQ req, K id) {
    E item = service().getById(id);
    Assert.notNull(item, Status.CODE.FAIL_NOT_FOUND);

    BeanUtils.copyProperties(req, item);

    item = service().updateById(item);

    return service().projection(voClass(), item);
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
  @CheckPermission("delete | supper")
  default Response<Boolean> dels(@PathVariable("tenant_id") String tenantId, @RequestParam List<K> ids) {
    service().removeByIds(ids);
    return Response.success(true);
  }

  /**
   * 状态设置
   * @param tenantId
   * @param ids
   * @param status
   * @return
   */
  @Operation(summary = "状态设置")
  @ParamTenant
  @GetMapping("status")
  @Transactional(rollbackFor = Exception.class)
  @CheckPermission("status | supper")
  default Response<Boolean> status(@PathVariable("tenant_id") String tenantId, @RequestParam List<K> ids,
                                   @RequestParam String status) {
    service().updateStatusByIds(ids, status);
    return Response.success(true);
  }

}
