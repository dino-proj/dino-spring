// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.sys.id.impl;

import java.util.UUID;

import org.dinospring.data.domain.IdService;
import org.dinospring.data.sql.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cody LU
 */

@Service
public class IdServiceImpl implements IdService {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  Dialect dialect;

  @Override
  public Long genId() {
    return jdbcTemplate.queryForObject(dialect.getSequenceNextValSql("sys_seq_id"), Long.class);
  }

  @Override
  public String genUUID() {
    if (dialect.supportUUID()) {
      return jdbcTemplate.queryForObject(dialect.getSelectUUIDSql(), String.class);
    } else {
      return UUID.randomUUID().toString();
    }
  }

}
