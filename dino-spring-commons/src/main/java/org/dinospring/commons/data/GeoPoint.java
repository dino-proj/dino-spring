package org.dinospring.commons.data;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoPoint implements Serializable {

  @Schema(description = "坐标点：纬度")
  @Parameter(name = "lat", description = "坐标点：纬度")
  private Double lat;

  @Parameter(name = "lon", description = "坐标点：经度")
  @Schema(description = "坐标点：经度")
  private Double lon;

}
