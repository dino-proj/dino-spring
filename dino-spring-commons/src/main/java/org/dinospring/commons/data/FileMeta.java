// Copyright 2021 dinospring.cn
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

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.dinospring.commons.json.annotation.JsonDiscriminator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author tuuboo
 */
@Data
@JsonInclude(Include.NON_NULL)
@JsonDiscriminator(property = "type")
public class FileMeta implements Serializable {

  @Schema(description = "文件类型")
  private FileTypes type;

  @Schema(description = "文件存放桶")
  private String bucket;

  @Schema(description = "文件存放路径")
  private String path;

  @Schema(description = "文件大小")
  private Long size;
}
