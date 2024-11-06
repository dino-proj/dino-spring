// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.core.modules.oss;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Cody Lu
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
