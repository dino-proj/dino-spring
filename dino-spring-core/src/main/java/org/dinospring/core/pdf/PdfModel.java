// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.pdf;

import lombok.Data;

/**
 * PDF模型抽象类，用于生成PDF文件。
 * @author Cody Lu
 * @date 2024-02-03 00:43:22
 */

@Data
public abstract class PdfModel {

  /**
   * 生成的PDF文件名，不带'.pdf'后缀。
   */
  private String fileName;

  /**
   * 是否只读，默认为false。
   */
  private boolean readOnly = false;

  /**
   * 是否设置密码保护，默认为false。
   */
  private boolean protect = false;

  /**
   * 打开文档的密码。
   */
  private String userPassword;

  /**
   * PDF文件的生产者。
   */
  private String producer;

  /**
   * PDF文件的创建者。
   */
  private String creator;

}
