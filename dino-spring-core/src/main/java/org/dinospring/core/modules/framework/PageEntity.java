package org.dinospring.core.modules.framework;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dinospring.core.converts.PageConfigJsonbConverter;
import org.dinospring.data.domain.TenantableEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldNameConstants
@Entity
@Table(name = "sys_frame_page")
public class PageEntity extends TenantableEntityBase<Long> {

  @Schema(title = "页面标题")
  @Column(name = "title", length = 64)
  private String title;

  @Schema(title = "页面对应的模板")
  @Column(name = "template_name", length = 64)
  private String templateName;

  @Schema(title = "页面的配置属性", type = "json")
  @Column(name = "config", columnDefinition = "jsonb")
  @Convert(converter = PageConfigJsonbConverter.class)
  private PageConfig config;

}
