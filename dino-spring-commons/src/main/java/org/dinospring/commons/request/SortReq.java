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

package org.dinospring.commons.request;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author tuuboo
 */

@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
public class SortReq {
  private static final char[] VALID_PROPERTY_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"
      .toCharArray();

  @Parameter(description = "排序，格式为: property(:asc|desc)。 默认按照asc升序， 支持多维度排序", name = "sort", array = @ArraySchema(schema = @Schema(type = "string", description = "格式为: property(:asc|desc)，默认为:asc")))
  @Setter
  private List<String> sort;

  public Sort sortable(String prefix) {
    if (CollectionUtils.isEmpty(sort)) {
      return Sort.unsorted();
    }

    var orders = sort.stream().map(s -> {
      var prop = StringUtils.substringBeforeLast(s, ":");
      var valid = StringUtils.isNotBlank(prop) && StringUtils.containsOnly(prop, VALID_PROPERTY_CHARS);
      Assert.isTrue(valid, "invalid sort property: " + s);
      return StringUtils.endsWithIgnoreCase(s, ":desc") ? Order.desc(prefix + prop) : Order.asc(prefix + prop);

    }).collect(Collectors.toList());

    return Sort.by(orders);
  }

  public Sort sortable() {
    return sortable("");
  }
}
