package org.dinospring.commons.request;

import javax.validation.constraints.Min;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
public class PageReq {

  /**
   * The Page.
   */
  @Min(0)
  @Parameter(description = "页码，从0开始 (0..N)", schema = @Schema(type = "integer", defaultValue = "0"))
  private Integer pn = 0;

  /**
   * The Size.
   */
  @Min(1)
  @Parameter(description = "页长，最小为1", schema = @Schema(type = "integer", defaultValue = "10"))
  private Integer pl = 10;

  public Pageable pageable() {
    return PageRequest.of(pn, pl);
  }

}
