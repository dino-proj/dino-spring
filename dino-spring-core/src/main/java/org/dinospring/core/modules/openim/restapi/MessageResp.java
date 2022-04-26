package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 *
 * @author Jack Liu
 * @date 2022-04-26 21:50:20
 */

@Data
public class MessageResp {

  /**
   * 发送者ID
   */
  @JsonProperty("sendTime")
  private String sendTime;

  /**
   * 发送者ID
   */
  @JsonProperty("serverMsgID")
  private String serverMsgId;

  /**
   * 发送者ID
   */
  @JsonProperty("clientMsgID")
  private String clientMsgId;
}
