// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.annotion.param;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody Lu
 */

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Parameter(in = ParameterIn.QUERY, description = "下一页游标，首页请传空值", name = "cursor", schema = @Schema(type = "string"), required = false)
@Parameter(in = ParameterIn.QUERY, description = "页长，最小为1", name = "pl", schema = @Schema(type = "integer", defaultValue = "10"))

public @interface ParamCursor {

}
