// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;

import cn.dinodev.spring.commons.json.annotation.JsonDiscriminator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件元数据基类，定义文件的基本属性
 *
 * @author Cody Lu
 */
@Data
@JsonInclude(Include.NON_NULL)
@JsonDiscriminator(property = "type")
@JsonTypeName("FILE")
public class FileMeta implements Serializable {

  @Schema(description = "文件类型")
  private final FileTypes type;

  @Schema(description = "文件存放桶")
  private String bucket;

  @Schema(description = "文件存放路径")
  private String path;

  @Schema(description = "文件大小")
  private Long size;

  /**
   * 默认构造函数。
   * <p>
   * 创建一个文件元数据对象，文件类型默认设置为FILE。
   * 通常用于反序列化或需要默认文件类型的场景。
   * </p>
   */
  public FileMeta() {
    this(FileTypes.FILE);
  }

  /**
   * 带文件类型的构造函数
   * 供子类使用，避免在构造函数中调用可重写的方法
   *
   * @param type 文件类型
   */
  protected FileMeta(FileTypes type) {
    this.type = type;
  }
}
