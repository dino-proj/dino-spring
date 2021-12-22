// Copyright 2021 dinospring.cn
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

package org.dinospring.core.sys.token;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.core.security.config.SecurityProperties;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.core.sys.login.config.LoginModuleProperties;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tuuboo
 */

@Slf4j
@Service
public class TokenService extends ServiceBase<TokenEntity, String> {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private LoginModuleProperties loginModuleProperties;

  @Autowired
  private SecurityProperties securityProperties;

  @Autowired
  private TokenRepository tokenRepository;

  @Override
  public CrudRepositoryBase<TokenEntity, String> repository() {
    return tokenRepository;
  }

  /**
   * 生成登录Token
   * @param princ 用户信息
   * @return
   */
  public Token genLoginToken(TokenPrincaple princ, String secretKey) {
    var token = new TokenEntity();
    long time = System.currentTimeMillis();
    token.setToken(generateToken(princ, secretKey, time));
    token.setRefreshToken(generateRefreshToken(princ, secretKey, time));
    token.setExpiresIn(loginModuleProperties.getToken().getLoginTokenExpiresIn().toSeconds());
    token.setRefreshExpiresIn(loginModuleProperties.getToken().getRefreshTokenExpiresIn().toSeconds());
    token.setTenantId(princ.getTenantId());

    token.setId(generateTokenId(princ));
    token.setUserId(princ.getUserId());
    token.setUserType(princ.getUserType());
    token.setUpdateAt(new Date(time));
    this.save(token);
    var t = this.projection(Token.class, token);
    try {
      t.setPrinc(Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsBytes(princ)));
      t.setAuthHeaderName(securityProperties.getHttpHeaderName());
    } catch (JsonProcessingException e) {
      log.error("Impossible!", e);
    }
    return t;
  }

  /**
   * 清除 token refresh token
   * @param princ
   */
  public void clearLoginToken(TokenPrincaple princ) {
    this.removeById(generateTokenId(princ));
  }

  /**
   * 校验登录Token
   * @param princ
   * @param token
   * @return
   */
  public boolean checkLoginToken(TokenPrincaple princ, String token) {
    var tokenEntity = tokenRepository.findById(generateTokenId(princ));
    //不存在
    if (tokenEntity.isEmpty()) {
      return false;
    }
    var t = tokenEntity.get();
    //已过期
    if (t.getUpdateAt().getTime() + t.getExpiresIn() * 1000 < System.currentTimeMillis()) {
      return Boolean.FALSE;
    }
    return t.getToken().equalsIgnoreCase(token);
  }

  /**
   * 刷新Token
   * @param princ
   * @param secretKey
   * @param refreshToken
   * @return
   */
  public Optional<Token> refreshLoginToken(TokenPrincaple princ, String secretKey, String refreshToken) {
    var tokenEntity = tokenRepository.findById(generateTokenId(princ));
    //不存在
    if (tokenEntity.isEmpty()) {
      return Optional.empty();
    }
    var t = tokenEntity.get();
    //已过期
    if (t.getUpdateAt().getTime() + t.getRefreshExpiresIn() * 1000 < System.currentTimeMillis()) {
      return Optional.empty();
    }
    if (!t.getToken().equalsIgnoreCase(refreshToken)) {
      return Optional.empty();
    }

    return Optional.ofNullable(genLoginToken(princ, secretKey));

  }

  private String generateTokenId(TokenPrincaple princ) {
    var idBuilder = new StringBuilder();
    idBuilder.append(princ.getTenantId()).append('_').append(princ.getUserId()).append('@').append(princ.getUserType());
    //允许多设备登录，则增加设备ID作为Token ID的一部分。
    if (loginModuleProperties.getToken().isAllowMutiDeviceLogin()) {
      idBuilder.append('_').append(princ.getGuid()).append('@').append(princ.getPlt());
    } else {
      idBuilder.append('_').append(princ.getPlt());
    }
    if (log.isDebugEnabled()) {
      log.debug("token ID before md5() {}", idBuilder.toString());
    }
    //计算MD5
    return new HmacUtils(HmacAlgorithms.HMAC_MD5, "dinospring")
        .hmacHex(idBuilder.toString().getBytes(StandardCharsets.UTF_8));
  }

  /**
   * 生成 token
   */
  private String generateToken(TokenPrincaple princ, String secretKey, long authAt) {
    return calculateToken(princ, secretKey, authAt,
        loginModuleProperties.getToken().getLoginTokenExpiresIn().toMillis());

  }

  /**
   * 生成refresh token
   * @param princ
   * @param secretKey
   * @param authAt
   * @return
   */
  private String generateRefreshToken(TokenPrincaple princ, String secretKey, long authAt) {
    return calculateToken(princ, StringUtils.reverse(secretKey), authAt,
        loginModuleProperties.getToken().getRefreshTokenExpiresIn().toMillis());
  }

  /**
   * 计算Token信息
   * @param princ
   * @param secretKey
   * @param authAt
   * @param siginTtlMs
   * @return
   */
  private String calculateToken(TokenPrincaple princ, String secretKey, long authAt, long siginTtlMs) {
    var params = List.of("tenant=" + princ.getTenantId(), "utype=" + princ.getUserType(), "plt=" + princ.getPlt(),
        "expiresIn=" + siginTtlMs, "authAt=" + authAt);

    var oriStr = params.stream().sorted().collect(Collectors.joining("&"));
    return new HmacUtils(HmacAlgorithms.HMAC_SHA_1, secretKey).hmacHex(oriStr);

  }

  /**
   * 对token签名认证
   */
  public String siginParams(String secretKey, Map<String, String> params) {
    String oriStr = params.entrySet().stream().map(kv -> kv.getKey() + "=" + kv.getValue()).sorted()
        .collect(Collectors.joining("&"));
    String hmacSha1Hex = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, secretKey).hmacHex(oriStr);
    if (log.isDebugEnabled()) {
      log.debug("sign check: str={}, sha1={}", oriStr, hmacSha1Hex);
    }
    return hmacSha1Hex;
  }

  /**
   * 用租户的秘钥验证参数签名
   * @param tenant 租户
   * @param siginToken 待验证的签名
   * @param params 签名的参数
   * @param signTimeMS 签名的时间
   * @return
   */
  public boolean verifyParamSigin(Tenant tenant, String siginToken, Map<String, String> params, long signTimeMS) {
    return verifyParam(siginToken, params, signTimeMS, tenant.getSecretKey(),
        loginModuleProperties.getToken().getSignTokenExpiresIn().toSeconds());
  }

  /**
   * 用用户自己的秘钥验证签名
   * @param <K> 用户的ID类型
   * @param user 用户
   * @param siginToken 待验证的签名
   * @param params 签名的参数
   * @param signTimeMS 签名的时间
   * @return
   */
  public <K extends Serializable> boolean verifyParamSigin(User<K> user, String siginToken, Map<String, String> params,
      long signTimeMS) {
    return verifyParam(siginToken, params, signTimeMS, user.getSecretKey(),
        loginModuleProperties.getToken().getSignTokenExpiresIn().toSeconds());
  }

  private boolean verifyParam(String siginToken, Map<String, String> params, long signTimeMS, String secretKey,
      long siginTtlSecondes) {

    long currentTime = System.currentTimeMillis();
    if (Math.abs(currentTime - signTimeMS) > siginTtlSecondes * 1000) {
      log.warn("token is expired， token:{}", siginToken);
      return false;
    }
    if (StringUtils.isEmpty(siginToken)) {
      return false;
    }
    var hmacSha1Hex = siginParams(secretKey, params);

    return hmacSha1Hex.equalsIgnoreCase(siginToken);
  }

}
