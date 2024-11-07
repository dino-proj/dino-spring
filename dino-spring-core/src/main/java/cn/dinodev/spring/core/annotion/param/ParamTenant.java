// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.annotion.param;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

/**
 *
 * @author Cody Lu
 */

@Target({ PARAMETER, METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Parameter(in = ParameterIn.PATH, name = "tenant_id", description = "租户ID，如/v*/{tenant_id}", required = true)
public @interface ParamTenant {

}
