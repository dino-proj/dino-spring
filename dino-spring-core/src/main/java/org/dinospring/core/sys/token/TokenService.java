// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.token;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.sys.Tenant;
import org.dinospring.commons.sys.User;
import org.dinospring.core.modules.login.config.LoginModuleProperties;
import org.dinospring.core.security.config.SecurityProperties;
import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.dinospring.data.sql.builder.SelectSqlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Token相关的服务
 *
 * @author Cody LU
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
    return this.tokenRepository;
  }

  private final JdbcAggregateTemplate jdbcAggregateTemplate;

  public TokenService(JdbcAggregateTemplate jdbcAggregateTemplate) {
    this.jdbcAggregateTemplate = jdbcAggregateTemplate;
  }

  /**
   * 生成登录Token
   *
   * @param princ 用户信息
   * @return
   */
  public Token genLoginToken(TokenPrincaple princ, String secretKey) {
    Optional<TokenEntity> dbOptional = this.repository().findById(this.generateTokenId(princ));
    TokenEntity token = new TokenEntity();
    long time = System.currentTimeMillis();
    token.setToken(this.generateToken(princ, secretKey, time));
    token.setRefreshToken(this.generateRefreshToken(princ, secretKey, time));
    token.setExpiresIn(this.loginModuleProperties.getToken().getLoginTokenExpiresIn().toSeconds());
    token.setRefreshExpiresIn(this.loginModuleProperties.getToken().getRefreshTokenExpiresIn().toSeconds());
    token.setTenantId(princ.getTenantId());

    token.setId(this.generateTokenId(princ));
    token.setUserId(princ.getUserId());
    token.setUserType(princ.getUserType());
    token.setUpdateAt(new Date(time));

    if (!dbOptional.isEmpty()) {
      this.save(token);
    } else {
      this.beforeSaveEntity(token);
      this.jdbcAggregateTemplate.insert(token);
    }

    var t = this.projection(Token.class, token);
    try {
      t.setPrinc(Base64.getUrlEncoder().encodeToString(this.objectMapper.writeValueAsBytes(princ)));
      t.setAuthHeaderName(this.securityProperties.getAuthHeaderName());
    } catch (JsonProcessingException e) {
      log.error("Impossible!", e);
    }
    return t;
  }

  /**
   * 清除 token refresh token
   *
   * @param princ
   */
  public void clearLoginToken(TokenPrincaple princ) {
    this.removeById(this.generateTokenId(princ));
  }

  /**
   * 校验登录Token
   *
   * @param princ
   * @param token
   * @return
   */
  public boolean checkLoginToken(TokenPrincaple princ, String token) {
    var tokenEntity = this.tokenRepository.findById(this.generateTokenId(princ));
    // 不存在
    if (tokenEntity.isEmpty()) {
      return false;
    }
    var t = tokenEntity.get();
    // 已过期
    if (t.getUpdateAt().getTime() + t.getExpiresIn() * 1000 < System.currentTimeMillis()) {
      return Boolean.FALSE;
    }
    return t.getToken().equalsIgnoreCase(token);
  }

  /**
   * 刷新Token
   *
   * @param princ
   * @param secretKey
   * @param refreshToken
   * @return
   */
  public Optional<Token> refreshLoginToken(TokenPrincaple princ, String secretKey, String refreshToken) {
    var tokenEntity = this.tokenRepository.findById(this.generateTokenId(princ));
    // 不存在
    if (tokenEntity.isEmpty()) {
      return Optional.empty();
    }
    var t = tokenEntity.get();
    // 已过期
    if (t.getUpdateAt().getTime() + t.getRefreshExpiresIn() * 1000 < System.currentTimeMillis()) {
      return Optional.empty();
    }
    if (!t.getRefreshToken().equalsIgnoreCase(refreshToken)) {
      return Optional.empty();
    }

    return Optional.ofNullable(this.genLoginToken(princ, secretKey));

  }

  public String generateTokenId(TokenPrincaple princ) {
    var idBuilder = new StringBuilder();
    idBuilder.append(princ.getTenantId()).append('_').append(princ.getUserId()).append('@').append(princ.getUserType());
    // 允许多设备登录，则增加设备ID作为Token ID的一部分。
    if (this.loginModuleProperties.isAllowMutiDeviceLogin()) {
      idBuilder.append('_').append(princ.getGuid()).append('@').append(princ.getPlt());
    } else {
      idBuilder.append('_').append(princ.getPlt());
    }
    if (log.isDebugEnabled()) {
      log.debug("token ID before md5() {}", idBuilder.toString());
    }
    // 计算MD5
    return new HmacUtils(HmacAlgorithms.HMAC_MD5, "dinospring")
        .hmacHex(idBuilder.toString().getBytes(StandardCharsets.UTF_8));
  }

  /**
   * 生成登录Token
   *
   * @param princ     用户信息
   * @param secretKey 秘钥
   * @param authAt    时间戳
   * @return
   */
  private String generateToken(TokenPrincaple princ, String secretKey, long authAt) {
    return this.calculateToken(princ, secretKey, authAt,
        this.loginModuleProperties.getToken().getLoginTokenExpiresIn().toMillis());

  }

  /**
   * 生成refresh token
   *
   * @param princ
   * @param secretKey
   * @param authAt
   * @return
   */
  private String generateRefreshToken(TokenPrincaple princ, String secretKey, long authAt) {
    return this.calculateToken(princ, StringUtils.reverse(secretKey), authAt,
        this.loginModuleProperties.getToken().getRefreshTokenExpiresIn().toMillis());
  }

  /**
   * 计算Token信息
   *
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
   *
   * @param tenant     租户
   * @param siginToken 待验证的签名
   * @param params     签名的参数
   * @param signTimeMS 签名的时间
   * @return
   */
  public boolean verifyParamSigin(Tenant tenant, String siginToken, Map<String, String> params, long signTimeMS) {
    return this.verifyParam(siginToken, params, signTimeMS, tenant.getSecretKey(),
        this.loginModuleProperties.getToken().getSignTokenExpiresIn().toSeconds());
  }

  /**
   * 用用户自己的秘钥验证签名
   *
   * @param <K>        用户的ID类型
   * @param user       用户
   * @param siginToken 待验证的签名
   * @param params     签名的参数
   * @param signTimeMS 签名的时间
   * @return
   */
  public <K extends Serializable> boolean verifyParamSigin(User<K> user, String siginToken, Map<String, String> params,
      long signTimeMS) {
    return this.verifyParam(siginToken, params, signTimeMS, user.getSecretKey(),
        this.loginModuleProperties.getToken().getSignTokenExpiresIn().toSeconds());
  }

  private boolean verifyParam(String siginToken, Map<String, String> params, long signTimeMS, String secretKey,
      long siginTtlSecondes) {

    long currentTime = System.currentTimeMillis();
    if (Math.abs(currentTime - signTimeMS) > siginTtlSecondes * 1000) {
      if (log.isDebugEnabled()) {
        log.debug("token is expired， token:{}", siginToken);
      }
      return false;
    }
    if (StringUtils.isEmpty(siginToken)) {
      return false;
    }
    var hmacSha1Hex = this.siginParams(secretKey, params);

    return hmacSha1Hex.equalsIgnoreCase(siginToken);
  }

  /**
   * 注销用户时，删除用户的所有Token
   * @param userId
   * @param userType
   * @return
   */
  public void clearUserToken(String userId, String userType) {
    SelectSqlBuilder sqlBuilder = repository().newSelect().eq("user_id", userId).eq("user_type", userType);
    List<TokenEntity> list = repository().queryList(sqlBuilder);
    if (CollectionUtils.isNotEmpty(list)) {
      list.forEach(token -> {
        this.removeById(token.getId());
      });
    }
  }
}
