// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.appclient;

import java.util.List;

import cn.dinodev.spring.commons.sys.Tenant;
import cn.dinodev.spring.core.service.impl.ServiceBase;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 应用客户端服务类
 * @author Cody Lu
 */

@Service
public class AppClientService extends ServiceBase<AppClientEntity, String> {

  @Autowired
  private AppClientRepository appClientRepository;

  /**
   * 查找应用客户端绑定的租户列表
   * @param appClientId 应用客户端ID
   * @return 租户列表
   */
  public List<Tenant> findBindTenants(String appClientId) {
    return projection(Tenant.class, appClientRepository.findBindTenants(appClientId));
  }

  @Override
  public CrudRepositoryBase<AppClientEntity, String> repository() {
    return appClientRepository;
  }

}
