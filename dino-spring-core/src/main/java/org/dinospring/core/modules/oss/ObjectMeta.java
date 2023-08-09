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

package org.dinospring.core.modules.oss;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody LU
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectMeta {

  @Schema(description = "对象名字")
  private String name;

  private boolean dir;

  @Schema(description = "对象大小")
  @Builder.Default
  private long size = 0L;

  @Schema(description = "最后修改时间")
  private Date updateAt;

  @Schema(description = "是否是文件夹")
  public boolean isDir() {
    return dir;
  }

  @Schema(description = "是否是文件")
  public boolean isFile() {
    return !dir;
  }

  public static ObjectMeta ofDir(String name, Date updateAt) {
    var meta = new ObjectMeta();
    meta.setDir(true);
    meta.setName(name);
    meta.setUpdateAt(updateAt);
    return meta;
  }

  public static ObjectMeta ofDir(String name, long updateAt) {
    return ofDir(name, new Date(updateAt));
  }

  public static ObjectMeta ofFile(String name, long size, Date updateAt) {
    var meta = new ObjectMeta();
    meta.setDir(false);
    meta.setSize(size);
    meta.setName(name);
    meta.setUpdateAt(updateAt);
    return meta;
  }

  public static ObjectMeta ofFile(String name, long size, long updateAt) {
    return ofFile(name, size, new Date(updateAt));
  }
}
