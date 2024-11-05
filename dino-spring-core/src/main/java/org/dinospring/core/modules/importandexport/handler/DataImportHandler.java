// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.importandexport.handler;

import java.util.Collection;

/**
 * @author JL
 * @Date: 2021/9/30
 */
public interface DataImportHandler<T> {

  /**
   * 导入的数据处理
   * @param dataList
   */
  void importData(Collection<T> dataList);
}
