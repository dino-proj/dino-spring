package org.dinospring.data.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 区分租户的实体类
 * @author tuuboo
 * @version v1.0
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class TenantableEntityBase<K extends Serializable> extends EntityBase<K> {
  /**
   * 代表系统的tenant ID；
   */
  public static final String TENANT_SYS = "SYS";

  /**
   * 租户ID
   */
  @Schema(description = "租户ID")
  @Column(name = "tenant_id", nullable = false, updatable = false, length = 16)
  private String tenantId;
}
