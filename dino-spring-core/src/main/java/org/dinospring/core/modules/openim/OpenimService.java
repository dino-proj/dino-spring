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
import org.dinospring.commons.utils.AsmUtils;
import org.dinospring.core.modules.openim.config.OpenimModuleProperties;
import org.dinospring.core.modules.openim.restapi.AccountCheck;
import org.dinospring.core.modules.openim.restapi.AccountCheckReq;
import org.dinospring.core.modules.openim.restapi.Group;
import org.dinospring.core.modules.openim.restapi.GroupReq;
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
   * 查询用户是否在IM中已经注册接口
   * @param accountCheckReq
   * @return
   */
  public AccountCheck accountCheck(AccountCheckReq accountCheckReq) {
    return post(AccountCheckReq.PATH, accountCheckReq, AccountCheck.class);
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
