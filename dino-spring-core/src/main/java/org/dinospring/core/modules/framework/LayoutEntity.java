package org.dinospring.core.modules.framework;

import java.io.Serializable;

import javax.persistence.Column;

import org.dinospring.commons.Scope;
import org.dinospring.data.domain.TenantableEntityBase;

import lombok.Data;

@Data
public class LayoutEntity<T extends Serializable> extends TenantableEntityBase<Long> {
  private String title;

  @Column(name = "access_scope", columnDefinition = "json", nullable = true)
  private Scope accessScope;

  @Column(name = "config", columnDefinition = "json", nullable = true)
  private T config;
}
