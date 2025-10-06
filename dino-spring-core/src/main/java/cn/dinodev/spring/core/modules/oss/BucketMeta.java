// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.oss;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody Lu
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketMeta {

  @Schema(description = "Bucket名字")
  private String name;

  @Schema(description = "Bucket创建时间")
  private Date createAt;

  /**
   * 创建 Bucket 元数据
   *
   * @param name Bucket 名称
   * @param createAt 创建时间
   * @return BucketMeta 实例
   */
  public static BucketMeta of(String name, Date createAt) {
    return new BucketMeta(name, createAt);
  }

  /**
   * 创建 Bucket 元数据
   *
   * @param name Bucket 名称
   * @param createAt 创建时间（时间戳）
   * @return BucketMeta 实例
   */
  public static BucketMeta of(String name, long createAt) {
    return new BucketMeta(name, new Date(createAt));
  }
}
