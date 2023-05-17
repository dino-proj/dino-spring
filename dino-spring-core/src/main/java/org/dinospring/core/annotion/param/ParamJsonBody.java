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

package org.dinospring.core.annotion.param;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 *
 * @author tuuboo
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
