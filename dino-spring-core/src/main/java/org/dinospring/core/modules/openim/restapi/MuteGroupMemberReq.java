package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Jack Liu
 * @date 2022-04-25 19:29:24
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MuteGroupMemberReq extends Request {

  public static final String PATH = "/group/mute_group_member";

  @JsonProperty("groupID")
  private String groupId;

  @JsonProperty("userID")
  private String userId;

  @JsonProperty("mutedSeconds")
  private Long mutedSeconds;

  @Override
  public boolean isRequiredToken() {
    return true;
  }

}
