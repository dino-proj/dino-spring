// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.oss;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody Lu
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectMeta {

  @Schema(description = "对象名字")
  private String name;

  private boolean dir;

  @Schema(description = "对象大小")
  private long size;

  @Schema(description = "最后修改时间")
  private Date updateAt;

  /**
   * 判断是否是文件夹
   * @return 如果是文件夹返回true，否则返回false
   */
  @Schema(description = "是否是文件夹")
  public boolean isDir() {
    return dir;
  }

  /**
   * 判断是否是文件
   * @return 如果是文件返回true，否则返回false
   */
  @Schema(description = "是否是文件")
  public boolean isFile() {
    return !dir;
  }

  /**
   * 创建目录对象元数据
   *
   * @param name 对象名称
   * @param updateAt 更新时间
   * @return ObjectMeta 实例
   */
  public static ObjectMeta ofDir(String name, Date updateAt) {
    var meta = new ObjectMeta();
    meta.setDir(true);
    meta.setName(name);
    meta.setUpdateAt(updateAt);
    return meta;
  }

  /**
   * 创建目录对象元数据
   *
   * @param name 对象名称
   * @param updateAt 更新时间（时间戳）
   * @return ObjectMeta 实例
   */
  public static ObjectMeta ofDir(String name, long updateAt) {
    return ofDir(name, new Date(updateAt));
  }

  /**
   * 创建文件对象元数据
   *
   * @param name 对象名称
   * @param size 文件大小
   * @param updateAt 更新时间
   * @return ObjectMeta 实例
   */
  public static ObjectMeta ofFile(String name, long size, Date updateAt) {
    var meta = new ObjectMeta();
    meta.setDir(false);
    meta.setSize(size);
    meta.setName(name);
    meta.setUpdateAt(updateAt);
    return meta;
  }

  /**
   * 创建文件对象元数据
   *
   * @param name 对象名称
   * @param size 文件大小
   * @param updateAt 更新时间（时间戳）
   * @return ObjectMeta 实例
   */
  public static ObjectMeta ofFile(String name, long size, long updateAt) {
    return ofFile(name, size, new Date(updateAt));
  }
}
