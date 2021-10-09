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

package org.dinospring.core.modules.oss;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tuuboo
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketMeta {

  @Schema(description = "Bucket名字")
  private String name;

  @Schema(description = "Bucket创建时间")
  private Date createAt;

  public static BucketMeta of(String name, Date createAt) {
    return new BucketMeta(name, createAt);
  }

  public static BucketMeta of(String name, long createAt) {
    return new BucketMeta(name, new Date(createAt));
  }
}
