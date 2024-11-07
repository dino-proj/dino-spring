// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.annotion.param;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 *
 * @author Cody Lu
 */

@RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "json", implementation = Object.class, example = "{}")))
public @interface ParamJsonBody {
  @AliasFor(attribute = "description", annotation = RequestBody.class)
  String description() default "可以是原始类型，比如数字、字符串、布尔等，也可以是数组、json对象";

  @AliasFor(attribute = "example", annotation = Schema.class)
  String example() default "{}";

  @AliasFor(attribute = "required", annotation = RequestBody.class)
  boolean required() default true;

  @AliasFor(attribute = "implementation", annotation = Schema.class)
  Class<?> implementation() default Object.class;

}
