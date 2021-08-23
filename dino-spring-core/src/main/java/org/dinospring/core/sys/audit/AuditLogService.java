package org.dinospring.core.sys.audit;

import org.dinospring.core.service.impl.ServiceBase;
import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService extends ServiceBase<AuditLogEntity, Long> {

  @Autowired
  private AuditLogRepository auditLogRepository;

  @Override
  public CURDRepositoryBase<AuditLogEntity, Long> getRepository() {
    return auditLogRepository;
  }

}
