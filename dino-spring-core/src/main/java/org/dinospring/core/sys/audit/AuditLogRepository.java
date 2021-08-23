package org.dinospring.core.sys.audit;

import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends CURDRepositoryBase<AuditLogEntity, Long> {

}
