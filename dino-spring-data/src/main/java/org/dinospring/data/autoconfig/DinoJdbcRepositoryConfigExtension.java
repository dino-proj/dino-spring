
package org.dinospring.data.autoconfig;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jdbc.repository.config.JdbcRepositoryConfigExtension;

public class DinoJdbcRepositoryConfigExtension extends JdbcRepositoryConfigExtension {

    @Override
    protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
        return List.of(jakarta.persistence.Table.class, org.springframework.data.relational.core.mapping.Table.class);
    }

}