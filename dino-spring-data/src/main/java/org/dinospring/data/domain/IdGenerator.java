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

package org.dinospring.data.domain;

import java.io.Serializable;
import java.util.Properties;

import org.dinospring.commons.context.ContextHelper;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * 根据ID类型生成ID
 * @author tuuboo
 */

public class IdGenerator extends IdentityGenerator implements Configurable {
  private String entityName;

  private IdService idService;

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
    var ep = session.getEntityPersister(entityName, object);
    var id = ep.getIdentifier(object, session);
    if ("string".equals(ep.getIdentifierType().getName())) {
      if (id == null) {
          id = idService.genIdStr();
      }
      return id;
    }
    return IdentifierGeneratorHelper.POST_INSERT_INDICATOR;
  }

  @Override
  public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
    entityName = params.getProperty(ENTITY_NAME);
    if (entityName == null) {
      throw new MappingException("no entity name");
    }
    idService = ContextHelper.findBean(IdService.class);
  }

}
