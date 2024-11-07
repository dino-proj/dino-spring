// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 *
 * @author Cody Lu
 */
@Data
@Embeddable
public class Address implements Serializable {

  @Schema(description = "省份")
  String province;

  @Schema(description = "城市")
  String city;

  @Schema(description = "区县")
  String area;

  @Schema(description = "街道")
  String street;

  @Schema(description = "详细地址")
  String detail;

  @Schema(description = "坐标点：经纬度")
  @JsonUnwrapped
  @Nullable
  private GeoPoint geoPoint;
}
