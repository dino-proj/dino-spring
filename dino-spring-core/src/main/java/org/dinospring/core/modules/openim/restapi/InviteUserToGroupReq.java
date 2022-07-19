package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: Jack Liu
 * @Date: 2022/5/31 20:50
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class InviteUserToGroupReq extends Request {

  public static final String PATH = "/group/invite_user_to_group";

  @JsonProperty(value = "groupID", required = true)
  private String groupId;

  @JsonProperty(value = "invitedUserIDList", required = true)
  private List<String> invitedUserIds;

  @JsonProperty("reason")
  private String reason;

  @Override
  public boolean isRequiredToken() {
    return true;
  }
}
