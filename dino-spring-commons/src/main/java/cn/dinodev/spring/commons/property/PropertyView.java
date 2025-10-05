// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.property;

/**
 * 为 @JsonView 定义的便捷工具类
 * @author Cody Lu
 */
public interface PropertyView {

  /**
   * 当显示简要信息的时候，使用，如下：
   * <pre>@JsonView(PropertyView.Summary.class)</pre>
   */
  interface Summary {
  }

  /**
   * 当显示详情信息的时候，使用，其包含了 Summary 注解的属性 如下：
   * <pre>@JsonView(PropertyView.Detail.class)</pre>
   */
  interface Detail extends Summary {
  }

  /**
   * 当更新的时候，使用，如下：
   * <pre>@JsonView(PropertyView.Update.class)</pre>
   */
  interface Update {
  }

  /**
   * 当插入的时候，使用，其包含了 Update 注解的属性 如下：
   * <pre>@JsonView(PropertyView.Insert.class)</pre>
   */
  interface Insert extends Update {
  }

}
