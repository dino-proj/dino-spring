// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.core.modules.scope.rule;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.apache.commons.codec.digest.DigestUtils;
import cn.dinodev.spring.core.modules.scope.ScopeRule;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cody Lu
 * @date 2022-03-31 16:46:15
 */

@Data
@NoArgsConstructor
public class IncludeExcludeRule<T extends Serializable & Comparable<T>> implements ScopeRule {
  private List<T> include;

  private List<T> exclude;

  @Override
  public String toJson(ObjectMapper objectMapper) {
    if (Objects.nonNull(include)) {
      Collections.sort(include);
    }
    if (Objects.nonNull(exclude)) {
      Collections.sort(exclude);
    }
    try {
      return "{"
          + "\"include\":"
          + objectMapper.writeValueAsString(Objects.requireNonNullElseGet(include, Collections::emptyList)) + ","
          + "\"exclude\":"
          + objectMapper.writeValueAsString(Objects.requireNonNullElseGet(exclude, Collections::emptyList))
          + "}";
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String hash() {
    if (Objects.nonNull(include)) {
      Collections.sort(include);
    }
    if (Objects.nonNull(exclude)) {
      Collections.sort(exclude);
    }
    return DigestUtils.md5Hex(new Gson().toJson(this));
  }
}
