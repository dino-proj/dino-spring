// Copyright 2022 dinodev.cn
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

package org.dinospring.core.modules.scope.rule;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.apache.commons.codec.digest.DigestUtils;
import org.dinospring.core.modules.scope.ScopeRule;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tuuboo
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
