// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.commons.data;

import java.io.Serializable;

import org.springframework.core.annotation.AnnotationUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonTypeName;

import cn.dinodev.spring.commons.json.annotation.JsonDiscriminator;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 可感知的类型接口
 * @author Cody Lu
 * @date 2024-01-30 23:44:19
 */

@JsonDiscriminator
public interface Typed extends Serializable {
  /**
   * 类型名
   * @return
   */
  @Schema(description = "类型名")
  @JsonProperty(value = "@t", access = Access.READ_ONLY)
  default String getTypeName() {
    var typeNameAnno = AnnotationUtils.findAnnotation(this.getClass(), JsonTypeName.class);

    if (typeNameAnno == null) {
      throw new IllegalStateException("类型名注解'@JsonTypeName'不存在");
    }

    return typeNameAnno.value();
  }
}
