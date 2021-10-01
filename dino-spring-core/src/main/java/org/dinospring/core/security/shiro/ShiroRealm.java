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

package org.dinospring.core.security.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.dinospring.commons.utils.Assert;
import org.dinospring.core.security.DinoPrincipal;
import org.dinospring.core.sys.tenant.TenantService;
import org.dinospring.core.sys.user.UserServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.CastUtils;

public class ShiroRealm extends AuthorizingRealm {

  @Autowired
  private TenantService tenantService;

  @Autowired
  private UserServiceProvider userServiceProvider;

  public ShiroRealm() {
    this.setAuthenticationTokenClass(ShiroAuthToken.class);
    //这里使用我们自定义的Matcher
    this.setCredentialsMatcher(new TokenCredentialsMatcher());
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

    return new SimpleAuthorizationInfo();
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    ShiroAuthToken authToken = CastUtils.cast(token);
    var princ = authToken.getPrinc();

    var user = userServiceProvider.resolveUserService(princ.getUserType()).getUserById(princ.getUserType(),
        princ.getUserId());
    Assert.state(user.isPresent(), "user[id={}, type={}] not found", princ.getUserId(), princ.getUserType());

    var tenant = tenantService.findTenantById(princ.getTenantId());
    Assert.state(tenant != null, "tenant[id={}] not found", princ.getTenantId());

    return new SimpleAccount(DinoPrincipal.of(user.orElse(null), tenant), token.getCredentials(), "jwtRealm");
  }
}
