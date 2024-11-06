// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.framework;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.dinospring.data.domain.TenantRowEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 *
 * @author Cody Lu
 * @author JL
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldNameConstants
@Entity
@Table(name = "sys_frame_page")
public class PageEntity extends TenantRowEntityBase<Long> {

  @Schema(title = "页面标题")
  @Column(name = "title", length = 64)
  private String title;

  @Schema(title = "页面对应的模板")
  @Column(name = "template_name", length = 64)
  private String templateName;

  @Schema(title = "页面的配置属性", type = "json")
  @Column(name = "config", columnDefinition = "jsonb")
  private PageConfig config;

}
