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

package org.dinospring.core.sys.dictionary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dinospring.commons.Orderable;
import org.dinospring.core.vo.VoImplBase;

import java.util.List;

/**
 *
 * @author Cody LU
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DictionaryVO extends VoImplBase<Long> {

  @Schema(description = "数据字典键值", maxLength = 50)
  private String key;

  @Schema(description = "数据字典项")
  private List<DictItemVO> items;

  @Schema(description = "备注", maxLength = 200)
  private String description;

  @Schema(description = "是否为系统预置（预置不可删除）")
  private Boolean deletable;

  @Schema(description = "是否可编辑")
  private Boolean editable;

  @Data
  public static class DictItemVO implements Orderable {

    @Schema(description = "字典键值", maxLength = 50)
    private String key;

    @Schema(description = "数据字典项的键值（编码）", maxLength = 100)
    private String itemKey;

    @Schema(description = "数据字典项的显示名称", maxLength = 100)
    private String itemName;

    @Schema(description = "数据字典项的图标", maxLength = 100)
    private String itemIcon;

    @Schema(description = "排序号")
    private Integer orderNum;
  }
}
