// Copyright 2021 dinospring.cn
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

package org.dinospring.commons.request;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tuuboo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
public class SortReq {

  @Parameter(description = "排序，格式为: property(:asc|desc)。 默认按照asc升序， 支持多维度排序", name = "sort", array = @ArraySchema(schema = @Schema(type = "string", description = "格式为: property(:asc|desc)，默认为:asc")))
  private List<String> sort;

  public Sort sortable() {
    if (CollectionUtils.isEmpty(sort)) {
      return Sort.unsorted();
    }

    var orders = sort.stream().map(s -> {
      var prop = StringUtils.substringBeforeLast(s, ":");
      return StringUtils.endsWithIgnoreCase(s, ":desc") ? Order.desc(prop) : Order.asc(prop);

    }).collect(Collectors.toList());

    return Sort.by(orders);
  }
}
