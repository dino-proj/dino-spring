package org.dinospring.core.modules.openim.restapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Jack Liu
 * @Date: 2022/4/20 16:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountCheckReq extends OpenIMRequest {
  public static final String PATH = "/user/account_check";

  /**
   * 需要check的用户userID数组，单次数量不超过100
   */
  @JsonProperty("checkUserIDList")
  private List<String> checkUserIdList;

  @Override
  public boolean isRequiredToken() {
    return true;
  }

}
