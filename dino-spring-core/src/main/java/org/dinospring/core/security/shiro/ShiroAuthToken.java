package org.dinospring.core.security.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.dinospring.core.sys.token.TokenPrincaple;

import lombok.Data;

@Data(staticConstructor = "of")
public class ShiroAuthToken implements AuthenticationToken {
  private final TokenPrincaple princ;

  private final String token;

  public ShiroAuthToken(TokenPrincaple princ, String token) {
    this.princ = princ;
    this.token = token;
  }

  @Override
  public Object getPrincipal() {
    return princ;
  }

  @Override
  public Object getCredentials() {
    return token;
  }

}