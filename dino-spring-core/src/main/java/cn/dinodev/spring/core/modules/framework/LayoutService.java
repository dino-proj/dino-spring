// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.framework;

import cn.dinodev.spring.core.service.impl.ServiceBase;
import cn.dinodev.spring.data.dao.CrudRepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Jack
 * @Date: 2021/11/24 10:13
 */
@Service
public class LayoutService extends ServiceBase<LayoutEntity, Long> {

  @Autowired
  private LayoutRepository layoutRepository;

  @Override
  public CrudRepositoryBase<LayoutEntity, Long> repository() {
    return layoutRepository;
  }
}
