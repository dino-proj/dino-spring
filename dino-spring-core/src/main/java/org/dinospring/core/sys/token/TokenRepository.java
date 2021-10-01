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

package org.dinospring.core.sys.token;

import java.util.HashMap;
import java.util.Map;

import org.dinospring.data.dao.CURDRepositoryBase;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CURDRepositoryBase<TokenEntity, String> {
  default void saveToken(String tenantId, String plt, String userId, Token token) {
    CACHE.TOKEN.setValue(token.getToken(), token);
    CACHE.REFRESH_TOKEN.setValue(token.getRefreshToken(), token);
  }

  default void removeToken(String plt, String tenantId, String uid) {

  }

  default Token getByToken(String tenantId, String uid, String token) {
    return CACHE.TOKEN.getValue(token);
  }

  default Token getByRefreshToken(String tenantId, String uid, String token) {
    return CACHE.REFRESH_TOKEN.getValue(token);
  }

  enum CACHE {
    TOKEN, REFRESH_TOKEN;

    private Map<String, Token> map = new HashMap<>();

    public Token getValue(String key) {
      return map.get(key);
    }

    public void setValue(String key, Token value) {
      map.put(key, value);
    }
  }
}
