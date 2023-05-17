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

package org.dinospring.core.modules.iam;

import org.dinospring.data.dao.CrudRepositoryBase;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tuuboo
 * @date 2022-05-04 22:41:25
 */

@Repository
public interface RoleRepository extends CrudRepositoryBase<RoleEntity, Long> {

}
