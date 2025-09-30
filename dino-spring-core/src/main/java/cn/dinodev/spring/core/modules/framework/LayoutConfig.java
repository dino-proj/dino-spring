// Copyright 2025 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.framework;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import cn.dinodev.spring.commons.json.annotation.JsonDiscriminator;

/**
 * @Author: Jack
 * @Date: 2021/11/25 16:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDiscriminator
public interface LayoutConfig extends Serializable {

}
