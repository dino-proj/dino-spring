package org.dinospring.core.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.dinospring.data.domain.TenantableEntityBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractOrg extends TenantableEntityBase<Long> {

  /**
   * 组织树根节点ID
   */
  public static final Long ROOT_ID = 0L;

  @Schema(description = "父节点ID")
  @Column(name = "parent_id", nullable = false)
  private Long parentId;

  @Schema(description = "节点名字")
  @Column(length = 100, nullable = false)
  private String name;

  @Schema(description = "节点深度")
  @Column(nullable = false)
  private Integer depth;

  @Schema(description = "节点ID级联，最后一个为本节点ID")
  @Column(name = "cascade_ids", nullable = false, columnDefinition = "jsonb")
  private Long[] cascadeIds;

  @Schema(description = "节点Name级联，最后一个为本节点Name")
  @Column(name = "cascade_names", nullable = false, columnDefinition = "jsonb")
  private String[] cascadeNames;

}
