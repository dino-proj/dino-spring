// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.data;

import java.util.Date;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody LU
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TimePeriod implements Range<Date> {

  @Schema(description = "开始时间, Unix时间戳，到毫秒")
  @Parameter(name = "begin", description = "开始时间, Unix时间戳，到毫秒")
  @Column(name = "begin_time")
  private Date begin;

  @Schema(description = "结束时间, Unix时间戳，到毫秒")
  @Parameter(name = "end", description = "束时间, Unix时间戳，到毫秒")
  @Column(name = "end_time")
  private Date end;
}
