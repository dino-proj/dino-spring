package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: Jack Liu
 * @Date: 2022/4/16 1:30
 */
@Data
public class GroupMember {

  /**
   * 用户 ID，必须保证IM内唯一
   */
  @JsonProperty("userID")
  private String userId;

  /**
   * 群内成员类型(1普通成员，2群主，3管理员)
   */
  @JsonProperty("roleLevel")
  private int roleLevel = 1;

}
