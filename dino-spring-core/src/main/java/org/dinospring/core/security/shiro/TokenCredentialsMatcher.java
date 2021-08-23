package org.dinospring.core.security.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.dinospring.core.sys.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.CastUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenCredentialsMatcher implements CredentialsMatcher {

  @Autowired
  private TokenService tokenService;

  /**
   * Matcher中直接调用工具包中的verify方法即可
   */
  @Override
  public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
    ShiroAuthToken authToken = CastUtils.cast(authenticationToken);

    var ret = tokenService.checkLoginToken(authToken.getPrinc(), authToken.getToken());
    if (!ret) {
      log.debug("Token chech faild. Princ={}, Token={}", authToken.getPrinc(), authToken.getToken());
    }

    return ret;
  }
}