// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 操作日志
 *
 * @author Cody Lu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldNameConstants
@Entity
@Table(name = "sys_auditlog")
public class AuditLogEntity extends TenantRowEntityBase<Long> {

  /**
   * 业务对象
   */
  @Schema(description = "业务对象")
  @Size(max = 100, message = "业务对象长度应小于100")
  @Column(name = "business")
  private String business;

  /**
   * 操作
   */
  @Size(max = 100, message = "操作长度应小于100")
  @Schema(description = "操作")
  @Column(length = 100)
  private String operation;

  /**
   * 用户类型
   */
  @Schema(description = "用户类型")
  @Size(max = 16, message = "用户类型长度应小于16")
  @Column(name = "user_type", length = 16)
  private String userType;

  /**
   * 用户ID
   */
  @Schema(description = "用户ID")
  @Column(name = "user_id")
  private String userId;

  /**
   * 用户显示名
   */
  @Size(max = 100, message = "用户显示名长度应小于100")
  @Schema(description = "用户显示名")
  @Column(name = "user_displayname", length = 100)
  private String userDisplayname;

  /**
   * 请求uri
   */
  @Size(max = 500, message = "请求uri长度应小于500")
  @Schema(description = "请求uri")
  @Column(name = "request_uri", length = 500)
  private String requestUri;

  /**
   * 请求method
   */
  @Size(max = 100, message = "请求method长度应小于100")
  @Schema(description = "请求method")
  @Column(name = "request_method", length = 100)
  private String requestMethod;

  /**
   * 请求参数
   */
  @Size(max = 1000, message = "请求参数长度应小于1000")
  @Schema(description = "请求参数")
  @Column(name = "request_params", length = 1000)
  private String requestParams;

  /**
   * 请求IP
   */
  @Size(max = 50, message = "请求IP长度应小于50")
  @Schema(description = "请求IP")
  @Column(name = "request_ip", length = 50)
  private String requestIp;

  /**
   * 响应状态码
   */
  @Schema(description = "响应状态码")
  @Column(name = "status_code")
  private int statusCode;

  /**
   * 异常信息
   */
  @Size(max = 1000, message = "用户类型长度应小于1000")
  @Schema(description = "异常信息")
  @Column(name = "error_msg", length = 1000)
  private String errorMsg;

}
