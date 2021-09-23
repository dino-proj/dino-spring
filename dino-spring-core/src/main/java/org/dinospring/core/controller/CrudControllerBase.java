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

public interface CrudControllerBase<S extends Service<E, K>, E extends EntityBase<K>, VO extends VoBase<K>, SRC extends SearchQuery, REQ, K extends Serializable> {

  S service();

  Class<VO> voClass();

  Class<E> entityClass();

  default VO processVo(VO vo) {
    return vo;
  }

  default List<VO> processVo(@Nonnull Collection<VO> voList) {
    voList.forEach(CrudControllerBase.this::processVo);
    if (List.class.isAssignableFrom(voList.getClass())) {
      return CastUtils.cast(voList);
    }
    return List.copyOf(voList);
  }

  default REQ processReq(String tenantId, PostBody<REQ> req, K id) {
    return req.getBody();
  }

  @Operation(summary = "列表")
  @ParamTenant
  @ParamPageable
  @PostMapping("list")
  default PageResponse<VO> list(@PathVariable("tenant_id") String tenantId, PageReq pageReq,
      @RequestBody PostBody<SRC> req) {

    var pageData = service().listPage(pageReq.pageable());

    return PageResponse.success(pageData, t -> processVo(service().projection(voClass(), t)));
  }

  @Operation(summary = "根据ID查询")
  @ParamTenant
  @Parameter(in = ParameterIn.QUERY, name = "id", required = true)
  @GetMapping("id")
  default Response<VO> getByid(@PathVariable("tenant_id") String tenantId, @RequestParam K id) {

    return Response.success(processVo(service().getById(id, voClass())));
  }

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

  @Operation(summary = "删除")
  @ParamTenant
  @GetMapping("delete")
  default Response<Boolean> dels(@PathVariable("tenant_id") String tenantId, @RequestParam List<K> ids) {
    service().removeByIds(ids);
    return Response.success(true);
  }

}
