// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.data;

import cn.dinodev.spring.commons.json.annotation.JsonDiscriminator;
import cn.dinodev.spring.core.modules.framework.Component;

/**
 *
 * @author Cody Lu
 */

@JsonDiscriminator
public interface DataProvider extends Component {

}
