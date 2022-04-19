package org.dinospring.core.modules.openim.restapi;

import lombok.Data;

/**
 * @Author: Jack Liu
 * @Date: 2022/4/16 1:24
 */
@Data
public class Group {

  private String creatorUserID;
  private String groupID;
  private String groupName;
  private int memberCount;
  private String ownerUserID;
}
