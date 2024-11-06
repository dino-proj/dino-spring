// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.audit;

import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CrudRepositoryBase;
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
