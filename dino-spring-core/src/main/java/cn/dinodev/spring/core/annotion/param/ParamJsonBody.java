// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.annotion.param;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * JSON 请求体参数注解，用于标注方法参数为JSON格式的请求体
 *
 * @author Cody Lu
 */

@RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "json", implementation = Object.class, example = "{}")))
public @interface ParamJsonBody {

  /**
   * 请求体描述信息
   * @return 描述信息字符串
   */
  @AliasFor(attribute = "description", annotation = RequestBody.class)
  String description() default "可以是原始类型，比如数字、字符串、布尔等，也可以是数组、json对象";

  /**
   * 请求体示例
   * @return 示例JSON字符串
   */
  @AliasFor(attribute = "example", annotation = Schema.class)
  String example() default "{}";

  /**
   * 是否必需参数
   * @return true表示必需，false表示可选
   */
  @AliasFor(attribute = "required", annotation = RequestBody.class)
  boolean required() default true;

  /**
   * 实现类类型
   * @return 实现类的Class对象
   */
  @AliasFor(attribute = "implementation", annotation = Schema.class)
  Class<?> implementation() default Object.class;

}
