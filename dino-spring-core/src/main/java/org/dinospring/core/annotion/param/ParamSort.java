// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.annotion.param;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody Lu
 */

@Target({ PARAMETER, METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Parameter(in = ParameterIn.QUERY, description = "排序，格式为: property(:asc|desc)。 默认按照asc升序， 支持多维度排序", name = "sort", array = @ArraySchema(schema = @Schema(type = "string", description = "格式为: property(:asc|desc)，默认为:asc")))
public @interface ParamSort {

}
