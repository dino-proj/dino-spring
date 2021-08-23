package org.dinospring.commons.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.botbrain.dino.sql.Range;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TimePeriod implements Range<Long> {

  @Schema(description = "开始时间, Unix时间戳，到毫秒")
  @Parameter(name = "begin", description = "开始时间, Unix时间戳，到毫秒")
  @Column(name = "begin_time")
  private Long begin;

  @Schema(description = "结束时间, Unix时间戳，到毫秒")
  @Parameter(name = "end", description = "束时间, Unix时间戳，到毫秒")
  @Column(name = "end_time")
  private Long end;
}
