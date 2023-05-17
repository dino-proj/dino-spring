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

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author tuuboo
 */

@Data
public class UploadMeta {

  @Schema(description = "上传的Oss服务器类型")
  private OssType ossType;

  @Schema(description = "上传URL地址")
  private String uploadUrl;

  private Method method;

  @Schema(description = "上传附带的Header头")
  private Map<String, String> headers;

  @Schema(description = "上传附带的data信息")
  private Map<String, Object> data;

  public enum OssType {
    //本地
    LOCAL,
    //阿里云
    ALI,
    //腾讯云
    TENCENT,
  }

  public enum Method {
    //PUT方法
    PUT,
    //POST方法
    POST
  }
}
