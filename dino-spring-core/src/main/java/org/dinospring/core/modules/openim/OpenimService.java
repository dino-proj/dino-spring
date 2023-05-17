// Copyright 2022 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.core.modules.openim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.utils.AsmUtils;
import org.dinospring.core.modules.openim.config.OpenimModuleProperties;
import org.dinospring.core.modules.openim.restapi.AccountCheck;
import org.dinospring.core.modules.openim.restapi.AccountCheckReq;
import org.dinospring.core.modules.openim.restapi.Group;
import org.dinospring.core.modules.openim.restapi.GroupReq;
import org.dinospring.core.modules.openim.restapi.InviteUserToGroupReq;
import org.dinospring.core.modules.openim.restapi.InviteUserToGroupResp;
import org.dinospring.core.modules.openim.restapi.MessageReq;
import org.dinospring.core.modules.openim.restapi.MessageResp;
import org.dinospring.core.modules.openim.restapi.MuteGroupMember;
import org.dinospring.core.modules.openim.restapi.MuteGroupMemberReq;
import org.dinospring.core.modules.openim.restapi.OpenIMRequest;
import org.dinospring.core.modules.openim.restapi.OpenIMResponse;
import org.dinospring.core.modules.openim.restapi.UserRegReq;
import org.dinospring.core.modules.openim.restapi.UserToken;
import org.dinospring.core.modules.openim.restapi.UserTokenReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * openim的restful接口实现
 * @author tuuboo
 * @date 2022-04-13 03:19:03
 */

@Service
@ConditionalOnBean(OpenimModuleProperties.class)
@Slf4j
public class OpenimService {

  @Autowired
  private RestTemplate restClient;

  @Autowired
  private OpenimModuleProperties properties;

  private static UserToken adminToken = null;

  /**
   * 获取用户token
   *
   * @param userId 要获取token的用户id
   * @param platform 用户登录或注册的平台类型，管理员填8
   * @return 用户token, 如果获取失败，返回null
   */
  public UserToken getUserToken(String userId, int platform) {
    var request = new UserTokenReq();
    request.setUserId(userId);
    request.setPlatform(platform);
    request.setSecret(properties.getSecret());
    return post(UserTokenReq.PATH, request, UserToken.class);
  }

  public synchronized String getAdminToken() {
    //判断token是否过期
    boolean isCreateToken = false;
    if (adminToken != null) {
      Long expiredTime = adminToken.getExpiredTime();
      long currentTime = System.currentTimeMillis() / 1000 + 5 * 60;
      if (expiredTime < currentTime) {
        isCreateToken = true;
      }
    } else {
      isCreateToken = true;
    }
    if (isCreateToken) {
      String adminId = properties.getAdminId();
      adminToken = getUserToken(adminId, 7);
    }
    return adminToken.getToken();
  }

  /**
   * 用户注册
   *
   * @param userId 用户id
   * @param nickname 用户昵称
   * @param platform 用户登录或注册的平台类型，管理员填8
   * @return 用户token, 如果获取失败
   */
  public UserToken registerUser(String userId, String nickname, int platform) {
    var request = new UserRegReq();
    request.setUserId(userId);
    request.setNickname(nickname);
    request.setPlatform(platform);
    request.setSecret(properties.getSecret());
    return post(UserRegReq.PATH, request, UserToken.class);
  }

  /**
   * 创建群组
   * @param groupReq
   * @return
   */
  public Group createGroup(GroupReq groupReq) {
    return post(GroupReq.PATH, groupReq, Group.class);
  }

  /**
   * 禁言群成员
   * @param muteGroupMemberReq
   * @return
   */
  public MuteGroupMember muteGroupMember(MuteGroupMemberReq muteGroupMemberReq) {
    return post(MuteGroupMemberReq.PATH, muteGroupMemberReq, MuteGroupMember.class);
  }

  /**
   * 邀请用户进群
   * @param inviteUserToGroupReq
   * @return
   */
  public List<InviteUserToGroupResp> inviteUserToGroup(InviteUserToGroupReq inviteUserToGroupReq) {
    return postForList(InviteUserToGroupReq.PATH, inviteUserToGroupReq, InviteUserToGroupResp.class);
  }

  /**
   * 管理员通过后台接口发送单聊群聊消息，可以以管理员身份发消息，也可以以其他用户的身份发消息，通过sendID区分
   * @param messageReq
   * @return
   */
  public MessageResp sendMsg(MessageReq messageReq) {
    String sendId = messageReq.getSendId();
    if (StringUtils.isBlank(sendId)) {
      sendId = properties.getAdminId();
    }
    messageReq.setSendId(sendId);
    return post(MessageReq.PATH, messageReq, MessageResp.class);
  }

  /**
   * 查询用户是否在IM中已经注册接口
   * @param accountCheckReq
   * @return
   */
  public List<AccountCheck> accountCheck(AccountCheckReq accountCheckReq) {
    return postForList(AccountCheckReq.PATH, accountCheckReq, AccountCheck.class);
  }

  protected String makeUrl(String path) {
    return properties.getUri() + path;
  }

  private <T> T post(String path, OpenIMRequest request, Class<T> respClass) {
    try {

      var newClassName = AsmUtils.className(OpenIMResponse.class, respClass);
      Class<OpenIMResponse<T>> cls = AsmUtils.defineGenericClass(newClassName, OpenIMResponse.class, respClass);

      HttpHeaders httpHeaders = new HttpHeaders();
      if (request.isRequiredToken()) {
        httpHeaders.set("token", getAdminToken());
      }

      HttpEntity<OpenIMRequest> httpEntity = new HttpEntity<>(request, httpHeaders);

      var resp = restClient.postForObject(makeUrl(path), httpEntity, cls);
      if (Objects.nonNull(resp)) {
        if (resp.getErrCode() == 0) {
          return resp.getData();
        } else {
          throw new IOException(String.format("ErrCode=%s, ErrMsg=%s", resp.getErrCode(), resp.getErrMsg()));
        }
      } else {
        throw new IOException("no Response");
      }
    } catch (Exception e) {
      log.error("gen class Response<{}> error", respClass.getSimpleName(), e);
      throw new RuntimeException(e);
    }
  }

  private <T> List<T> postForList(String path, OpenIMRequest request, Class<T> respClass) {
    return (List<T>) post(path, request, new ArrayList<T>(1).getClass());
  }
}
