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
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonView;

import org.dinospring.auth.Operations;
import org.dinospring.auth.annotation.CheckPermission;
import org.dinospring.commons.property.PropertyView;
import org.dinospring.commons.request.PageReq;
import org.dinospring.commons.request.PostBody;
import org.dinospring.commons.request.SortReq;
import org.dinospring.commons.response.PageResponse;
import org.dinospring.commons.response.Response;
import org.dinospring.commons.response.Status;
import org.dinospring.commons.utils.Assert;
import org.dinospring.commons.utils.ProjectionUtils;
import org.dinospring.core.annotion.param.ParamPageable;
import org.dinospring.core.annotion.param.ParamSort;
import org.dinospring.core.annotion.param.ParamTenant;
import org.dinospring.core.service.CustomQuery;
import org.dinospring.core.service.Service;
import org.dinospring.core.vo.VoBase;
import org.dinospring.data.domain.EntityBase;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

/**
 * @param S service??????
 * @param E entity??????
 * @param VO vo??????
 * @param SRC search query??????
 * @param REQ add???update???post body??????
 * @param K entity???????????????
 *
 * @author tuuboo
 * @author JL
 */

public interface CrudControllerBase<S extends Service<E, K>, E extends EntityBase<K>, VO extends VoBase<K>, SRC extends CustomQuery, REQ, K extends Serializable>
    extends ControllerBase<S, E, VO, K> {

  /**
   * ???VO??????????????????????????????
   * @param vo
   * @return
   */
  default VO processVo(@Nonnull VO vo) {
    return vo;
  }

  /**
   * ???VoList??????VO??????????????????????????????
   * @param voList
   * @return
   */
  default List<VO> processVoList(@Nonnull Collection<VO> voList) {
    return voList.stream().map(this::processVo).collect(Collectors.toList());
  }

  /**
   * ????????????????????????????????????
   * @param tenantId ??????ID
   * @param req ????????????
   * @param id Entity???ID?????????????????????id?????????null??????????????????id???null
   * @return
   */
  default REQ processReq(String tenantId, PostBody<REQ> req, K id) {
    return req.getBody();
  }

  /**
   * ????????????
   * @param tenantId ??????ID
   * @param pageReq ????????????
   * @param sortReq ??????
   * @param req ????????????
   * @return
   */
  @Operation(summary = "??????")
  @ParamTenant
  @ParamPageable
  @ParamSort
  @PostMapping("list")
  @JsonView(PropertyView.Summary.class)
  @CheckPermission(Operations.LIST)
  default PageResponse<VO> list(@PathVariable("tenant_id") String tenantId, PageReq pageReq, SortReq sortReq,
      @RequestBody PostBody<SRC> req) {
    var pageable = pageReq.pageable(sortReq, "t.");

    var query = req.getBody();
    var pageData = query == null ? service().listPage(pageable, voClass())
        : service().listPage(query, pageable, voClass());

    return PageResponse.success(pageData, this::processVoList);
  }

  /**
   * ??????ID????????????
   * @param tenantId ??????ID
   * @param id Entity???ID
   * @return
   */
  @Operation(summary = "??????ID??????")
  @ParamTenant
  @Parameter(in = ParameterIn.QUERY, name = "id", required = true)
  @GetMapping("id")
  @JsonView(PropertyView.Detail.class)
  @CheckPermission(Operations.READ)
  default Response<VO> getById(@PathVariable("tenant_id") String tenantId, @RequestParam K id) {

    return Response.success(processVo(service().getById(id, voClass())));
  }

  /**
   * ??????Entity
   * @param tenantId ??????ID
   * @param req ????????????
   * @return
   */
  @Operation(summary = "??????")
  @ParamTenant
  @PostMapping("add")
  @Transactional(rollbackFor = Exception.class)
  @JsonView(PropertyView.Detail.class)
  @CheckPermission(Operations.CREATE)
  default Response<VO> add(@PathVariable("tenant_id") String tenantId,
      @RequestBody @Validated(PropertyView.Insert.class) @JsonView(PropertyView.Insert.class) PostBody<REQ> req) {

    var body = processReq(tenantId, req, null);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    VO vo = add(body);

    return Response.success(processVo(vo));
  }

  /**
   * ??????
   * @param req
   * @return
   */
  default VO add(REQ req) {
    E item = service().projection(entityClass(), req);

    item = service().save(item);

    return service().projection(voClass(), item);
  }

  /**
   * ??????Entity??????
   * @param tenantId ??????ID
   * @param id ?????????Entity???ID
   * @param req ????????????
   * @return
   */
  @Operation(summary = "??????")
  @ParamTenant
  @PostMapping("update")
  @Transactional(rollbackFor = Exception.class)
  @JsonView(PropertyView.Detail.class)
  @CheckPermission(Operations.UPDATE)
  default Response<VO> update(@PathVariable("tenant_id") String tenantId, @RequestParam K id,
      @RequestBody @Validated(PropertyView.Update.class) @JsonView(PropertyView.Update.class) PostBody<REQ> req) {

    var body = processReq(tenantId, req, id);

    Assert.notNull(body, Status.CODE.FAIL_INVALID_PARAM);

    VO vo = update(body, id);
    return Response.success(processVo(vo));
  }

  /**
   * ??????
   * @param req
   * @param id
   * @return
   */
  default VO update(REQ req, K id) {
    E item = service().getById(id);
    Assert.notNull(item, Status.CODE.FAIL_NOT_FOUND);

    ProjectionUtils.projectPropertiesWithView(req, item, PropertyView.Update.class);

    item = service().updateById(item);

    return service().projection(voClass(), item);
  }

  /**
   * ??????Entity??????
   * @param tenantId ??????ID
   * @param ids ????????????Entity?????????ID
   * @return
   */
  @Operation(summary = "??????")
  @ParamTenant
  @GetMapping("delete")
  @CheckPermission(Operations.DELETE)
  default Response<Boolean> dels(@PathVariable("tenant_id") String tenantId, @RequestParam List<K> ids) {
    service().removeByIds(ids);
    return Response.success(true);
  }

  /**
   * ????????????
   * @param tenantId
   * @param ids
   * @param status
   * @return
   */
  @Operation(summary = "????????????")
  @ParamTenant
  @GetMapping("status")
  @Transactional(rollbackFor = Exception.class)
  @CheckPermission(Operations.EXECUTE)
  default Response<Boolean> status(@PathVariable("tenant_id") String tenantId, @RequestParam List<K> ids,
      @RequestParam String status) {
    service().updateStatusByIds(ids, status);
    return Response.success(true);
  }

}
