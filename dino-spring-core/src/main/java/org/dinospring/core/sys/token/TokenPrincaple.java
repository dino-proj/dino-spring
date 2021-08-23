package org.dinospring.core.sys.token;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPrincaple implements Serializable {
  private String tenantId;
  private String userType;
  private String userId;
  private String plt;
  private String guid;
}
