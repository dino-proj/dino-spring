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
 * 地址信息类，包含省份、城市、区县、详细地址等信息
 * @author Cody Lu
 */
@Data
@Embeddable
public class Address implements Serializable {

  @Schema(description = "省份")
  private String province;

  @Schema(description = "城市")
  private String city;

  @Schema(description = "区县")
  private String area;

  @Schema(description = "街道")
  private String street;

  @Schema(description = "详细地址")
  private String detail;

  @Schema(description = "坐标点：经纬度")
  @JsonUnwrapped
  @Nullable
  private GeoPoint geoPoint;
}
