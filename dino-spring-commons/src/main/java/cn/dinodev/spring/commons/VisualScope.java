// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 可见范围配置类，用于控制数据的可见性范围
 * 
 * @author JL
 * @since 2021-10-25
 */
@Schema(description = "可见范围")
@Data
public class VisualScope implements Serializable {

  @Schema(description = "人员")
  private List<String> user;
  @Schema(name = "user_type", description = "id:user_type")
  private List<String> userType;
  @Schema(description = "部门")
  private List<Long> dept;
  @Schema(description = "职位")
  private List<Long> post;
  @Schema(description = "组")
  private List<Long> group;
  @Schema(description = "公司")
  private List<Long> company;

  /**
   * 检查可见范围配置是否为空。
   * <p>
   * 当所有的范围配置（用户、部门、职位、组、公司）都为空时，返回true。
   * 这通常表示没有设置任何可见性限制。
   * </p>
   * 
   * @return 如果所有范围配置都为空则返回true，否则返回false
   */
  public boolean beEmpty() {
    return CollectionUtils.isEmpty(user) && CollectionUtils.isEmpty(dept) && CollectionUtils.isEmpty(post)
        && CollectionUtils.isEmpty(group) && CollectionUtils.isEmpty(company);
  }
}
