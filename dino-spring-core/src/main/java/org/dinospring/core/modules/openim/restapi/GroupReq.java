package org.dinospring.core.modules.openim.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: Jack Liu
 * @Date: 2022/4/16 1:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GroupReq extends Request {
  public static final String PATH = "/group/create_group";

  /**
   * 群主UserID
   */
  @JsonProperty("ownerUserID")
  private String ownerUserId;

  /**
   * 群类型 目前统一填0
   */
  @JsonProperty("groupType")
  private int groupType = 0;

  /**
   * 群名称
   */
  @JsonProperty("groupName")
  private String groupName;

  /**
   * 群公告
   */
  @JsonProperty("notification")
  private String notification;

  /**
   * 群介绍
   */
  @JsonProperty("introduction")
  private String introduction;

  /**
   * 用户头像或者群头像url，根据上下文理解
   */
  @JsonProperty("faceURL")
  private String faceURL;

  /**
   * 扩展字段，用户可自行扩展，建议封装成 JSON 字符串
   */
  @JsonProperty("ex")
  private String ex;

  /**
   * 指定初始群成员
   */
  @JsonProperty("memberList")
  private List<GroupMember> memberList;

  @Override
  public boolean isRequiredToken() {
    return true;
  }
}
