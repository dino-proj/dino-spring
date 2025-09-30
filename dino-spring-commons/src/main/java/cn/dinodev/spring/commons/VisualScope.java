// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author JL
 * @Date: 2021/10/25
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

  public boolean beEmpty() {
    return CollectionUtils.isEmpty(user) && CollectionUtils.isEmpty(dept) && CollectionUtils.isEmpty(post)
        && CollectionUtils.isEmpty(group) && CollectionUtils.isEmpty(company);
  }
}
