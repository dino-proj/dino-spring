// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.sys.audit;

import cn.dinodev.spring.data.dao.CrudRepositoryBase;

/**
 *
 * @author Cody Lu
 */

public interface AuditLogRepository extends CrudRepositoryBase<AuditLogEntity, Long> {

}
