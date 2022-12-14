// Copyright 2022 dinospring.cn
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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
import org.dinospring.core.modules.openim.restapi.Request;
import org.dinospring.core.modules.openim.restapi.Response;
import org.dinospring.core.modules.openim.restapi.UserRegReq;
import org.dinospring.core.modules.openim.restapi.UserToken;
import org.dinospring.core.modules.openim.restapi.UserTokenReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * openim???restful????????????
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
   * ????????????token
   *
   * @param userId ?????????token?????????id
   * @param platform ???????????????????????????????????????????????????8
   * @return ??????token, ???????????????????????????null
   */
  public UserToken getUserToken(String userId, int platform) {
    var request = new UserTokenReq();
    request.setUserId(userId);
    request.setPlatform(platform);
    request.setSecret(properties.getSecret());
    return post(UserTokenReq.PATH, request, UserToken.class);
  }

  public synchronized String getAdminToken() {
    //??????token????????????
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
   * ????????????
   *
   * @param userId ??????id
   * @param nickname ????????????
   * @param platform ???????????????????????????????????????????????????8
   * @return ??????token, ??????????????????
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
   * ????????????
   * @param groupReq
   * @return
   */
  public Group createGroup(GroupReq groupReq) {
    return post(GroupReq.PATH, groupReq, Group.class);
  }

  /**
   * ???????????????
   * @param muteGroupMemberReq
   * @return
   */
  public MuteGroupMember muteGroupMember(MuteGroupMemberReq muteGroupMemberReq) {
    return post(MuteGroupMemberReq.PATH, muteGroupMemberReq, MuteGroupMember.class);
  }

  /**
   * ??????????????????
   * @param inviteUserToGroupReq
   * @return
   */
  public List<InviteUserToGroupResp> inviteUserToGroup(InviteUserToGroupReq inviteUserToGroupReq) {
    return post(InviteUserToGroupReq.PATH, inviteUserToGroupReq, new ArrayList<InviteUserToGroupResp>().getClass());
  }

  /**
   * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????sendID??????
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
   * ?????????????????????IM?????????????????????
   * @param accountCheckReq
   * @return
   */
  public List<AccountCheck> accountCheck(AccountCheckReq accountCheckReq) {
    List<AccountCheck> checkList = new ArrayList<>();
    ArrayList<LinkedHashMap<String, String>> post = post(AccountCheckReq.PATH, accountCheckReq, new ArrayList<LinkedHashMap<String, String>>().getClass());
    if (CollectionUtils.isNotEmpty(post)){
      post.forEach(stringStringLinkedHashMap -> {
        var accountCheck = new AccountCheck();
        accountCheck.setAccountStatus(stringStringLinkedHashMap.get("accountStatus"));
        accountCheck.setUserId(stringStringLinkedHashMap.get("userID"));
        checkList.add(accountCheck);
      });
    }
    return checkList;
  }

  protected String makeUrl(String path) {
    return properties.getUri() + path;
  }

  private <T> T post(String path, Request request, Class<T> respClass) {
    try {

      var newClassName = AsmUtils.className(Response.class, respClass);
      Class<Response<T>> cls = AsmUtils.defineGenericClass(newClassName, Response.class, respClass);

      if (request.isRequiredToken()) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("token", getAdminToken());
        HttpEntity<Request> httpEntity = new HttpEntity<>(request, httpHeaders);
        var resp = restClient.postForObject(makeUrl(path), httpEntity, cls);
        return resp.getData();
      }

      var resp = restClient.postForObject(makeUrl(path), request, cls);
      return resp.getData();
    } catch (Exception e) {
      log.error("gen class Response<{}> error", respClass.getSimpleName(), e);
      throw new RuntimeException(e);
    }
  }

}
