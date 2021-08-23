package org.dinospring.commons.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
