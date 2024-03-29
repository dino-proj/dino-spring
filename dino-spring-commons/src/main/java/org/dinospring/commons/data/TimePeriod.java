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
