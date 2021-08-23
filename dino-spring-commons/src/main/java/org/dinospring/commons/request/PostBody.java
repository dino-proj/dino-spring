package org.dinospring.commons.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostBody<T> {

  @Schema(description = "登录用户的ID")
  private String uid;

  @Schema(description = "登录用户的类型")
  private String utype;

  @Schema(description = "用户的GUID", nullable = true)
  private String guid;

  @Schema(description = "客户端的平台，PC、APP、WX、H5等")
  private String plt;

  @Schema(description = "Session ID", nullable = true)
  private String sid;

  @Schema(description = "Post数据")
  private T body;
}
