package org.dinospring.core.modules.openim.restapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Jack Liu
 * @date 2022-04-26 21:50:14
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageReq extends Request {

  public static final String PATH = "/manager/send_msg";

  /**
   * 发送者ID
   */
  @JsonProperty(value = "sendID", required = true)
  private String sendId;

  /**
   * 接收者ID，单聊为用户ID，如果是群聊，则不填
   */
  @JsonProperty(value = "recvID")
  private String recvId;

  /**
   * 群聊ID，如果为单聊，则不填
   */
  @JsonProperty("groupID")
  private String groupId;

  /**
   * 发送者昵称
   */
  @JsonProperty("senderNickname")
  private String senderNickName;

  /**
   * 发送者头像
   */
  @JsonProperty("senderFaceURL")
  private String senderFaceURL;

  /**
   * 发送者平台号，模拟用户发送时填写， 1->IOS,2->Android,3->Windows,4->OSX,5->Web,5->MiniWeb,7->Linux
   */
  @JsonProperty("senderPlatformID")
  private String senderPlatformId;

  /**
   * 当聊天类型为群聊时，使用@指定强推用户userID列表
   */
  @JsonProperty("forceList")
  private List<String> forceList;

  /**
   * 消息的具体内容，内部是json 对象
   */
  @JsonProperty(value = "content", required = true)
  private Content content;

  /**
   * 消息类型，101表示文本，102表示图片
   */
  @JsonProperty(value = "contentType", required = true)
  private int contentType = 101;

  /**
   * 发送的消息是单聊还是群聊,单聊为1，群聊为2
   */
  @JsonProperty(value = "sessionType", required = true)
  private int sessionType = 1;

  /**
   * 改字段设置为true时候，发送的消息服务器不会存储，接收者在线才会收到并存储到本地，不在线该消息丢失，当消息的类型为113->typing时候，接收者本地也不会做存储
   */
  @JsonProperty("isOnlineOnly")
  private boolean isOnlineOnly = false;

  /**
   * 发送的消息是单聊还是群聊,单聊为1，群聊为2
   */
  @JsonProperty("offlinePushInfo")
  private OfflinePushInfo offlinePushInfo;

  @Override
  public boolean isRequiredToken() {
    return false;
  }

  @Data
  public class OfflinePushInfo {
    /**
    * 推送的标题
    */
    @JsonProperty("title")
    private String title;

    /**
     * 推送的具体描述
     */
    @JsonProperty("desc")
    private String desc;

    /**
     * 扩展字段
     */
    @JsonProperty("ex")
    private String ex;

    /**
     * IOS的推送声音
     */
    @JsonProperty("iOSPushSound")
    private String iOSPushSound;

    /**
     * IOS推送消息是否计入桌面图标未读数
     */
    @JsonProperty("iOSBadgeCount")
    private boolean iOSBadgeCount;
  }

  @Data
  public static class Content {

    @JsonProperty("text")
    private String text;
  }
}
