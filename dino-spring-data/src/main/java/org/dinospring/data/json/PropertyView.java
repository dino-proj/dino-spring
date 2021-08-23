package org.dinospring.data.json;

import lombok.experimental.UtilityClass;

/**
 * 为 @JsonView 定义的便捷工具类
 */
@UtilityClass
public class PropertyView {

  /**
   * 当显示简要信息的时候，使用，如下：
   * <pre>@JsonView(PropertyView.OnSummary.class)</pre>
   */
  public static interface OnSummary {
  }

  /**
   * 当显示详情信息的时候，使用，其包含了 OnSummary 注解的属性 如下：
   * <pre>@JsonView(PropertyView.OnDetail.class)</pre>
   */
  public static interface OnDetail extends OnSummary {
  }

  public static interface OnUpdate {
  }

  public static interface OnInsert extends OnUpdate {
  }

}
