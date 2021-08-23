package org.dinospring.core.modules.appclient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dinospring.data.domain.EntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_app_client")
public class AppClientEntity extends EntityBase<String> {
  @Column(length = 32)
  String name;
}
