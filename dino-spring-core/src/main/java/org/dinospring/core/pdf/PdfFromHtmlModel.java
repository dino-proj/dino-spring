// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.pdf;

import org.dinospring.commons.function.Resolver;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 将HTML转换为PDF的工具类。
 *
 * @author Cody Lu
 * @date 2024-02-02 05:47:19
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PdfFromHtmlModel extends PdfModel {

  /**
   * HTML内容。
   */
  private String html;

  /**
   * 工作目录，用于存放生成的PDF文件。
   */
  private String workDir;

  private Resolver<String> uriResolver;

}
