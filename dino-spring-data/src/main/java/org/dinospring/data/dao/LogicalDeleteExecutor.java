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

package org.dinospring.data.dao;

import java.io.Serializable;

import org.dinospring.data.domain.EntityBase;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@Cacheable
@NoRepositoryBean
public interface LogicalDeleteExecutor<T extends EntityBase<K>, K extends Serializable> {
  @Modifying
  @Query("update #{#entityName} set deleted=true where id=:id")
  @CacheEvict(cacheNames = "repo", key = "#{#entityName}'.id:'+#key")
  void logicalDeleteById(@Param("id") K id);
}
