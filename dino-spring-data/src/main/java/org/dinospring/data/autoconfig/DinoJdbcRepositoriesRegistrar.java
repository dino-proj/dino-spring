package org.dinospring.data.autoconfig;

import java.lang.annotation.Annotation;

import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.stereotype.Component;

@Component
public class DinoJdbcRepositoriesRegistrar extends AbstractRepositoryConfigurationSourceSupport {

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableJdbcRepositories.class;
	}

	@Override
	protected Class<?> getConfiguration() {
		return EnableJdbcRepositoriesConfiguration.class;
	}

	@Override
	protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
		return new DinoJdbcRepositoryConfigExtension();
	}

	@EnableJdbcRepositories
	private static class EnableJdbcRepositoriesConfiguration {

	}

}
