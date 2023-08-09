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

package org.dinospring.commons.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 *
 * @author Cody LU
 */
@Data
@Embeddable
public class Address implements Serializable {

  @Schema(description = "省份")
  String province;

  @Schema(description = "城市")
  String city;

  @Schema(description = "详细地址")
  String street;

  @Schema(description = "坐标点：经纬度")
  @JsonUnwrapped
  private GeoPoint geoPoint;
}
