// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.audit;

import cn.dinodev.spring.core.service.impl.ServiceBase;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cody Lu
 */

@Service
public class AuditLogService extends ServiceBase<AuditLogEntity, Long> {

  @Autowired
  private AuditLogRepository auditLogRepository;

  @Override
  public CrudRepositoryBase<AuditLogEntity, Long> repository() {
    return auditLogRepository;
  }

}
