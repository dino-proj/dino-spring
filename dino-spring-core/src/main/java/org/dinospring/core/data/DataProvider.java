// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.data;

import org.dinospring.commons.json.annotation.JsonDiscriminator;
import org.dinospring.core.modules.framework.Component;

/**
 *
 * @author Cody Lu
 */

@JsonDiscriminator
public interface DataProvider extends Component {

}
