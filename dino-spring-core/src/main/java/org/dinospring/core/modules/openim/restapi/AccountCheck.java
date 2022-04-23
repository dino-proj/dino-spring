package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

/**
 * @Author: Jack Liu
 * @Date: 2022/4/20 16:56
 */
@Data
public class AccountCheck {

  @JsonProperty("userID")
  private String userId;

  @JsonProperty("accountStatus")
  private String accountStatus;

  public enum AccountStatus{
    /**
     * 已注册
     */
    REGISTERED("registered","已注册"),
    /**
     * 未注册
     */
    UNREGISTERED("unregistered","未注册");

    @Getter
    private String status;

    @Getter
    private String desc;

    AccountStatus(String status, String desc) {
      this.status = status;
      this.desc = desc;
    }
  }
}
