package org.dinospring.data.domain;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class IdGenerator extends IdentityGenerator implements IdentifierGenerator, Configurable {
  private String entityName;

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
    var ep = session.getEntityPersister(entityName, object);
    var id = ep.getIdentifier(object, session);
    if ("string".equals(ep.getIdentifierType().getName())) {
      if (id == null) {
        throw new IdentifierGenerationException(
            "ids for String must be manually assigned before calling save(): " + entityName);
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
  }

}
