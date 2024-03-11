// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.pdf;

import org.dinospring.commons.function.Resolver;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PdfFromHtmlModel类是一个继承自PdfModel的模型类，用于生成基于HTML内容的PDF文件。
 *
 * @author Cody Lu
 * @date 2024-02-02 05:47:19
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ResponseBody
public class PdfFromHtmlModel extends PdfModel {

  /**
   * HTML内容。
   */
  private String html;

  /**
   * 工作目录，用于存放生成的PDF文件。
   */
  private String workDir;

  /**
   * URI解析器，用于解析HTML中的资源URI。
   */
  private Resolver<String> uriResolver;

}
