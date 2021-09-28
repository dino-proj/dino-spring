package org.dinospring.core.modules.oss.config;

import javax.annotation.Nonnull;

import lombok.Data;

@Data
public class LocalOssProperties {

  /**
   * 本地存储文件夹路径
   */
  @Nonnull
  private String baseDir;
}