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

import org.dinospring.commons.utils.AsmUtils;
import org.dinospring.core.modules.openim.config.OpenimModuleProperties;
import org.dinospring.core.modules.openim.restapi.Request;
import org.dinospring.core.modules.openim.restapi.Response;
import org.dinospring.core.modules.openim.restapi.UserRegReq;
import org.dinospring.core.modules.openim.restapi.UserToken;
import org.dinospring.core.modules.openim.restapi.UserTokenReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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

  /**
   * 获取用户token
   *
   * @param userId 要获取token的用户id
   * @param platform 用户登录或注册的平台类型，管理员填8
   * @return 用户token, 如果获取失败，返回null
   */
  UserToken getUserToken(String userId, int platform) {
    var request = new UserTokenReq();
    request.setUserId(userId);
    request.setPlatform(platform);
    request.setSecret(properties.getSecret());
    return post(UserTokenReq.PATH, request, UserToken.class);
  }

  /**
   * 用户注册
   *
   * @param userId 用户id
   * @param nickname 用户昵称
   * @param platform 用户登录或注册的平台类型，管理员填8
   * @return 用户token, 如果获取失败
   */
  UserToken registerUser(String userId, String nickname, int platform) {
    var request = new UserRegReq();
    request.setUserId(userId);
    request.setNickname(nickname);
    request.setPlatform(platform);
    request.setSecret(properties.getSecret());
    return post(UserRegReq.PATH, request, UserToken.class);
  }

  protected String makeUrl(String path) {
    return properties.getUri() + path;
  }

  private <T> T post(String path, Request request, Class<T> respClass) {
    try {
      var newClassName = AsmUtils.className(Response.class, respClass);
      Class<Response<T>> cls = AsmUtils.defineGenericClass(newClassName, Response.class, respClass);

      var resp = restClient.postForObject(makeUrl(path), request, cls);
      return resp.getData();
    } catch (Exception e) {
      log.error("gen class Response<{}> error", respClass.getSimpleName(), e);
      throw new RuntimeException(e);
    }
  }

}