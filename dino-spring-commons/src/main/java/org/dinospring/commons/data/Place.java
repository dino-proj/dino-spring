package org.dinospring.commons.data;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Embeddable
public class Place {

  @Schema(description = "地点名称")
  String name;

  @Schema(description = "坐标点：经纬度")
  @JsonUnwrapped
  private GeoPoint geoPoint;
}
