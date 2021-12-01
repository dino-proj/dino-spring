/*
 *  Copyright 2021 dinospring.cn
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dinospring.core.modules.framework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import org.dinospring.commons.annotion.AnnotionedJsonTypeIdResolver;

import java.io.Serializable;

/**
 * @Author: Jack
 * @Date: 2021/11/25 16:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
  use = JsonTypeInfo.Id.CUSTOM,
  include = JsonTypeInfo.As.PROPERTY,
  visible = true,
  property = "@t"
)
@JsonTypeIdResolver(AnnotionedJsonTypeIdResolver.class)
public interface LayoutConfig extends Serializable {

}
