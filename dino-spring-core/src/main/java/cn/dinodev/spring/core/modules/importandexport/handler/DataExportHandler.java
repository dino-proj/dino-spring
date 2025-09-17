// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.importandexport.handler;

import java.util.List;

/**
 * @author JL
 * @Date: 2021/9/30
 */
public interface DataExportHandler<E> {

  /**
   * 导出数据处理
   * @return
   */
  List<E> importData();
}
