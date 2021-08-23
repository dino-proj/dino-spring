package org.dinospring.commons.sys;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public interface UserType extends Serializable {

  @Schema(description = "用户类型字符串")
  String getType();

  @Schema(description = "所有用户类型", hidden = true)
  List<UserType> allTypes();

}
