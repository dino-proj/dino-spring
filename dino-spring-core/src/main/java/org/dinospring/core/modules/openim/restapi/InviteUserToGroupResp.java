package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: Jack Liu
 * @Date: 2022/5/31 20:55
 */

@Data
public class InviteUserToGroupResp {

  @JsonProperty("userID")
  private String userId;

  @JsonProperty("result")
  private Integer result;

}
