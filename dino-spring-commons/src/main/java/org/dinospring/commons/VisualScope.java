/*
 *  Copyright 2021 dinospring.cn
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dinospring.commons;

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
  @Schema(name = "user_type",description = "id:user_type")
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
