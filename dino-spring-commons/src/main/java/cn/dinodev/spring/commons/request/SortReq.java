// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.request;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
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
 * @author Cody Lu
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

  /**
   * 根据给定的前缀生成排序对象。
   *
   * 示例:
   * <pre>
   * SortReq sortReq = new SortReq(List.of("name:desc", "age:asc"));
   * Sort sort = sortReq.sortable("user_");
   * // 生成的排序对象将会按照 user_name 降序和 user_age 升序排序
   * </pre>
   *
   * @param prefix 排序属性的前缀
   * @return 排序对象
   *
   */
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

  /**
   * 生成排序对象。
   *
   * <p>
   * 示例:
   * <pre>
   * SortReq sortReq = new SortReq(List.of("name:desc", "age:asc"));
   * Sort sort = sortReq.sortable();
   * // 生成的排序对象将会按照 name 降序和 age 升序排序
   * </pre>
   * </p>
   *
   * @return 排序对象
   *
   */
  public Sort sortable() {
    return sortable("");
  }
}
