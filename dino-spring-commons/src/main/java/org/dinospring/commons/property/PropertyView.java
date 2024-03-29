// Copyright 2021 dinodev.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.commons.property;

import lombok.experimental.UtilityClass;

/**
 * 为 @JsonView 定义的便捷工具类
 * @author Cody LU
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
