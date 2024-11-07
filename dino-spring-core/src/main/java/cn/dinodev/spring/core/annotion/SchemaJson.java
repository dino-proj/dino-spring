// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Cody Lu
 */

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Schema(implementation = Object.class, type = "json", description = "可以是原始类型，比如数字、字符串、布尔等，也可以是数组、json对象")
public @interface SchemaJson {

}
