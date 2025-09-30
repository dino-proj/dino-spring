// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * 地点信息类，包含地点名称和坐标信息
 * @author Cody Lu
 */

@Data
@Embeddable
public class Place {

  @Schema(description = "地点名称")
  String name;

  @Schema(description = "坐标点：经纬度")
  @JsonUnwrapped
  private GeoPoint geoPoint;
}
