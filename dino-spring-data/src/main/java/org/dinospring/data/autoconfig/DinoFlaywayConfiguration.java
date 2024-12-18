// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.data.autoconfig;

import java.sql.DatabaseMetaData;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.dinospring.commons.context.ContextHelper;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cody Lu
 * @date 2022-05-05 02:32:26
 */

@Configuration
@ConditionalOnClass(Flyway.class)
@ConditionalOnProperty(prefix = "spring.flyway", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(FlywayProperties.class)
@EnableAutoConfiguration(exclude = { FlywayAutoConfiguration.class })
@Slf4j
public class DinoFlaywayConfiguration implements ApplicationListener<ApplicationReadyEvent> {

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    var configuration = ContextHelper.findBean(FluentConfiguration.class);
    var javaMigrations = ContextHelper.getApplicationContext().getBeansOfType(JavaMigration.class).values();
    log.info("--->> flyway: exec java migration, count:{}", javaMigrations.size());
    javaMigrations.forEach(
        migration -> log.info("   -- {}, class:{}", this.buildJavaMigrationName(migration),
            migration.getClass().getName()));
    configuration.javaMigrations(javaMigrations.toArray(new JavaMigration[javaMigrations.size()]));
    var flyway = configuration.load();
    flyway.migrate();
  }

  @Bean
  public FlywayConfigurationCustomizer flaywayConfigurationCustomizer() {
    return config -> {
      config.baselineOnMigrate(true);
      config.failOnMissingLocations(false);
    };
  }

  @Bean
  public FluentConfiguration flywayConfiguration(FlywayProperties properties, ResourceLoader resourceLoader,
      ObjectProvider<DataSource> dataSource, @FlywayDataSource ObjectProvider<DataSource> flywayDataSource,
      ObjectProvider<FlywayConfigurationCustomizer> fluentConfigurationCustomizers,
      ObjectProvider<Callback> callbacks) {
    log.info("--->> flyway: setup FlywayConfiguration");
    FluentConfiguration configuration = new FluentConfiguration(resourceLoader.getClassLoader());
    this.configureDataSource(configuration, properties, flywayDataSource.getIfAvailable(), dataSource.getIfUnique());
    this.configureProperties(configuration, properties);
    List<Callback> orderedCallbacks = callbacks.orderedStream().collect(Collectors.toList());
    this.configureCallbacks(configuration, orderedCallbacks);
    fluentConfigurationCustomizers.orderedStream().forEach(customizer -> customizer.customize(configuration));
    this.configureFlywayCallbacks(configuration, orderedCallbacks);
    return configuration;

  }

  private void configureDataSource(FluentConfiguration configuration, FlywayProperties properties,
      DataSource flywayDataSource, DataSource dataSource) {
    DataSource migrationDataSource = this.getMigrationDataSource(properties, flywayDataSource, dataSource);
    configuration.dataSource(migrationDataSource);
  }

  private DataSource getMigrationDataSource(FlywayProperties properties, DataSource flywayDataSource,
      DataSource dataSource) {
    if (flywayDataSource != null) {
      return flywayDataSource;
    }
    if (properties.getUrl() != null) {
      DataSourceBuilder<?> builder = DataSourceBuilder.create().type(SimpleDriverDataSource.class);
      builder.url(properties.getUrl());
      this.applyCommonBuilderProperties(properties, builder);
      return builder.build();
    }
    if (properties.getUser() != null && dataSource != null) {
      DataSourceBuilder<?> builder = DataSourceBuilder.derivedFrom(dataSource)
          .type(SimpleDriverDataSource.class);
      this.applyCommonBuilderProperties(properties, builder);
      return builder.build();
    }
    Assert.state(dataSource != null, "Flyway migration DataSource missing");
    return dataSource;
  }

  private void applyCommonBuilderProperties(FlywayProperties properties, DataSourceBuilder<?> builder) {
    builder.username(properties.getUser());
    builder.password(properties.getPassword());
    if (StringUtils.hasText(properties.getDriverClassName())) {
      builder.driverClassName(properties.getDriverClassName());
    }
  }

  private void configureProperties(FluentConfiguration configuration, FlywayProperties properties) {
    PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
    this.failOnMissingLocations(configuration, properties, map);
    this.createSchemas(configuration, properties, map);
    this.validateMigrationNaming(configuration, properties, map);
  }

  private void failOnMissingLocations(FluentConfiguration configuration, FlywayProperties properties,
      PropertyMapper map) {
    String[] locations = new LocationResolver(configuration.getDataSource())
        .resolveLocations(properties.getLocations()).toArray(new String[0]);
    configuration.failOnMissingLocations(properties.isFailOnMissingLocations());
    map.from(locations)
        .to(configuration::locations);
    map.from(properties.getEncoding())
        .to(configuration::encoding);
    map.from(properties.getConnectRetries())
        .to(configuration::connectRetries);
    // No method reference for compatibility with Flyway < 7.15
    map.from(properties.getConnectRetriesInterval())
        .to(interval -> configuration.connectRetriesInterval((int) interval.getSeconds()));
    // No method reference for compatibility with Flyway 6.x
    map.from(properties.getLockRetryCount())
        .to(configuration::lockRetryCount);
    // No method reference for compatibility with Flyway 5.x
    map.from(properties.getDefaultSchema())
        .to(configuration::defaultSchema);
    map.from(properties.getSchemas())
        .as(StringUtils::toStringArray)
        .to(configuration::schemas);
  }

  private void createSchemas(FluentConfiguration configuration, FlywayProperties properties, PropertyMapper map) {
    configuration.createSchemas(properties.isCreateSchemas());
    map.from(properties.getTable())
        .to(configuration::table);
    // No method reference for compatibility with Flyway 5.x
    map.from(properties.getTablespace())
        .to(configuration::tablespace);
    map.from(properties.getBaselineDescription())
        .to(configuration::baselineDescription);
    map.from(properties.getBaselineVersion())
        .to(configuration::baselineVersion);
    map.from(properties.getInstalledBy())
        .to(configuration::installedBy);
    map.from(properties.getPlaceholders())
        .to(configuration::placeholders);
    map.from(properties.getPlaceholderPrefix())
        .to(configuration::placeholderPrefix);
    map.from(properties.getPlaceholderSuffix())
        .to(configuration::placeholderSuffix);
    map.from(properties.isPlaceholderReplacement())
        .to(configuration::placeholderReplacement);
    map.from(properties.getSqlMigrationPrefix())
        .to(configuration::sqlMigrationPrefix);
    map.from(properties.getSqlMigrationSuffixes())
        .as(StringUtils::toStringArray)
        .to(configuration::sqlMigrationSuffixes);
    map.from(properties.getSqlMigrationSeparator())
        .to(configuration::sqlMigrationSeparator);
    map.from(properties.getRepeatableSqlMigrationPrefix())
        .to(configuration::repeatableSqlMigrationPrefix);
    map.from(properties.getTarget())
        .to(configuration::target);
    map.from(properties.isBaselineOnMigrate())
        .to(configuration::baselineOnMigrate);
    map.from(properties.isCleanDisabled())
        .to(configuration::cleanDisabled);
    map.from(properties.isCleanOnValidationError())
        .to(configuration::cleanOnValidationError);
    map.from(properties.isGroup())
        .to(configuration::group);

    map.from(properties.isMixed())
        .to(configuration::mixed);
    map.from(properties.isOutOfOrder())
        .to(configuration::outOfOrder);
    map.from(properties.isSkipDefaultCallbacks())
        .to(configuration::skipDefaultCallbacks);
    map.from(properties.isSkipDefaultResolvers())
        .to(configuration::skipDefaultResolvers);
  }

  private void validateMigrationNaming(FluentConfiguration configuration, FlywayProperties properties,
      PropertyMapper map) {
    configuration.validateMigrationNaming(properties.isValidateMigrationNaming());
    map.from(properties.isValidateOnMigrate())
        .to(configuration::validateOnMigrate);
    map.from(properties.getInitSqls())
        .whenNot(CollectionUtils::isEmpty)
        .as(initSqls -> StringUtils.collectionToDelimitedString(initSqls, "\n"))
        .to(configuration::initSql);
    map.from(properties.getScriptPlaceholderPrefix())
        .to(configuration::scriptPlaceholderPrefix);
    map.from(properties.getScriptPlaceholderSuffix())
        .to(configuration::scriptPlaceholderSuffix);
    // Pro properties
    map.from(properties.getBatch())
        .to(configuration::batch);
    map.from(properties.getDryRunOutput())
        .to(configuration::dryRunOutput);
    map.from(properties.getErrorOverrides())
        .to(configuration::errorOverrides);
    map.from(properties.getLicenseKey())
        .to(configuration::licenseKey);
    map.from(properties.getStream())
        .to(configuration::stream);
    map.from(properties.getUndoSqlMigrationPrefix())
        .to(configuration::undoSqlMigrationPrefix);
    // No method reference for compatibility with Flyway 6.x
    map.from(properties.getCherryPick())
        .to(configuration::cherryPick);
    // No method reference for compatibility with Flyway 6.x
    map.from(properties.getJdbcProperties())
        .whenNot(Map::isEmpty)
        .to(configuration::jdbcProperties);
    // No method reference for compatibility with Flyway 6.x
    map.from(properties.getKerberosConfigFile())
        .to(configuration::kerberosConfigFile);
    // No method reference for compatibility with Flyway 6.x
    map.from(properties.getOutputQueryResults())
        .to(configuration::outputQueryResults);
    // No method reference for compatibility with Flyway 6.x
    map.from(properties.getSkipExecutingMigrations())
        .to(configuration::skipExecutingMigrations);
    // No method reference for compatibility with Flyway < 7.8
    map.from(properties.getIgnoreMigrationPatterns())
        .whenNot(List::isEmpty)
        .to(ignoreMigrationPatterns -> configuration
            .ignoreMigrationPatterns(ignoreMigrationPatterns.toArray(new String[0])));
    // No method reference for compatibility with Flyway version < 7.9
    map.from(properties.getDetectEncoding())
        .to(configuration::detectEncoding);
  }

  private void configureCallbacks(FluentConfiguration configuration, List<Callback> callbacks) {
    if (!callbacks.isEmpty()) {
      configuration.callbacks(callbacks.toArray(new Callback[0]));
    }
  }

  private void configureFlywayCallbacks(FluentConfiguration flyway, List<Callback> callbacks) {
    if (!callbacks.isEmpty()) {
      flyway.callbacks(callbacks.toArray(new Callback[0]));
    }
  }

  private String buildJavaMigrationName(JavaMigration javaMigration) {
    var nameBuilder = new StringBuilder();
    if (Objects.nonNull(javaMigration.getVersion())) {
      nameBuilder.append('V').append(javaMigration.getVersion());
    } else {
      nameBuilder.append('R');
    }
    nameBuilder.append("__").append(javaMigration.getDescription());
    return nameBuilder.toString();
  }

  private static class LocationResolver {

    private static final String VENDOR_PLACEHOLDER = "{vendor}";

    private final DataSource dataSource;

    LocationResolver(DataSource dataSource) {
      this.dataSource = dataSource;
    }

    List<String> resolveLocations(List<String> locations) {
      if (this.usesVendorLocation(locations)) {
        DatabaseDriver databaseDriver = this.getDatabaseDriver();
        return this.replaceVendorLocations(locations, databaseDriver);
      }
      return locations;
    }

    private List<String> replaceVendorLocations(List<String> locations, DatabaseDriver databaseDriver) {
      if (databaseDriver == DatabaseDriver.UNKNOWN) {
        return locations;
      }
      String vendor = databaseDriver.getId();
      return locations.stream().map(location -> location.replace(VENDOR_PLACEHOLDER, vendor))
          .collect(Collectors.toList());
    }

    private DatabaseDriver getDatabaseDriver() {
      try {
        String url = JdbcUtils.extractDatabaseMetaData(this.dataSource, DatabaseMetaData::getURL);
        return DatabaseDriver.fromJdbcUrl(url);
      } catch (MetaDataAccessException ex) {
        throw new IllegalStateException(ex);
      }

    }

    private boolean usesVendorLocation(Collection<String> locations) {
      for (String location : locations) {
        if (location.contains(VENDOR_PLACEHOLDER)) {
          return true;
        }
      }
      return false;
    }

  }

  /**
   * Convert a String or Number to a {@link MigrationVersion}.
   */
  static class StringOrNumberToMigrationVersionConverter implements GenericConverter {

    private static final Set<ConvertiblePair> CONVERTIBLE_TYPES;

    static {
      Set<ConvertiblePair> types = new HashSet<>(2);
      types.add(new ConvertiblePair(String.class, MigrationVersion.class));
      types.add(new ConvertiblePair(Number.class, MigrationVersion.class));
      CONVERTIBLE_TYPES = Collections.unmodifiableSet(types);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
      return CONVERTIBLE_TYPES;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
      String value = ObjectUtils.nullSafeToString(source);
      return MigrationVersion.fromVersion(value);
    }

  }

}
