// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.iam;

import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cody LU
 * @date 2022-05-04 22:42:20
 */

@Service
public class RoleService extends ServiceBase<RoleEntity, Long> {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public CrudRepositoryBase<RoleEntity, Long> repository() {
    return roleRepository;
  }

}
