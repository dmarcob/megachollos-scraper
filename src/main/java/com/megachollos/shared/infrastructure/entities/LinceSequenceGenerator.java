package com.megachollos.shared.infrastructure.entities;

import java.util.Properties;
import org.hibernate.MappingException;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class LinceSequenceGenerator extends SequenceStyleGenerator {

  @Override
  public void configure(Type type, Properties params, ServiceRegistry serviceRegistry)
      throws MappingException {
    String entityName = params.getProperty(JPA_ENTITY_NAME);
    if (entityName == null) {
      throw new IllegalStateException("Entity name must not be null");
    }

    params.setProperty(SEQUENCE_PARAM, entityName + DEF_SEQUENCE_SUFFIX);
    params.setProperty(INCREMENT_PARAM, "1");
    params.setProperty(INITIAL_PARAM, "1");

    super.configure(type, params, serviceRegistry);
  }
}
