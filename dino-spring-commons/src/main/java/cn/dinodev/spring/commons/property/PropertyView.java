// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.property;

import lombok.experimental.UtilityClass;

/**
 * 为 @JsonView 定义的便捷工具类
 * @author Cody Lu
 */
@UtilityClass
public class PropertyView {

  /**
   * 当显示简要信息的时候，使用，如下：
   * <pre>@JsonView(PropertyView.OnSummary.class)</pre>
   */
  public static interface Summary {
  }

  /**
   * 当显示详情信息的时候，使用，其包含了 OnSummary 注解的属性 如下：
   * <pre>@JsonView(PropertyView.OnDetail.class)</pre>
   */
  public static interface Detail extends Summary {
  }

  public static interface Update {
  }

  public static interface Insert extends Update {
  }

}
