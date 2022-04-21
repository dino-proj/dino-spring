package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: Jack Liu
 * @Date: 2022/4/20 16:50
 */
@Data
public class AccountCheckReq extends Request {
  public static final String PATH = "/manager/account_check";

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
