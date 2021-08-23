package org.dinospring.core.security;

import java.io.Serializable;

import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;

import lombok.Data;

@Data(staticConstructor = "of")
public class DinoPrincipal implements Serializable {
  private final User<?> user;
  private final Tenant tenant;
}
