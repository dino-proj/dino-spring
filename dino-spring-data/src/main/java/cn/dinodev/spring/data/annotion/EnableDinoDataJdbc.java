// Copyright 2023 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package cn.dinodev.spring.data.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.dinodev.spring.data.autoconfig.DinoDataAutoConfiguration;
import cn.dinodev.spring.data.autoconfig.DinoDataJdbcConfiguration;
import cn.dinodev.spring.data.dao.impl.DinoJdbcRepositoryBase;
import cn.dinodev.spring.data.jdbc.DinoJdbcRepositoriesRegistrar;
import cn.dinodev.spring.data.jdbc.DinoJdbcRepositoryFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * Annotion for Enable Dino Spring Data Jdbc
 *
 * @author Cody Lu
 * @date 2023-12-26 23:23:55
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ DinoDataAutoConfiguration.class, DinoDataJdbcConfiguration.class, DinoJdbcRepositoriesRegistrar.class })
public @interface EnableDinoDataJdbc {

  /**
   * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
   * {@code @EnableJdbcRepositories("org.my.pkg")} instead of
   * {@code @EnableJdbcRepositories(basePackages="org.my.pkg")}.
   */
  String[] value() default {};

  /**
   * Base packages to scan for annotated components. {@link #value()} is an alias for (and mutually exclusive with) this
   * attribute. Use {@link #basePackageClasses()} for a type-safe alternative to String-based package names.
   */
  String[] basePackages() default {};

  /**
   * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components. The
   * package of each class specified will be scanned. Consider creating a special no-op marker class or interface in
   * each package that serves no purpose other than being referenced by this attribute.
   */
  Class<?>[] basePackageClasses() default {};

  /**
   * Specifies which types are eligible for component scanning. Further narrows the set of candidate components from
   * everything in {@link #basePackages()} to everything in the base packages that matches the given filter or filters.
   */
  Filter[] includeFilters() default {};

  /**
   * Specifies which types are not eligible for component scanning.
   */
  Filter[] excludeFilters() default {};

  /**
   * Returns the postfix to be used when looking up custom repository implementations. Defaults to {@literal Impl}. So
   * for a repository named {@code PersonRepository} the corresponding implementation class will be looked up scanning
   * for {@code PersonRepositoryImpl}.
   */
  String repositoryImplementationPostfix() default "Impl";

  /**
   * Configures the location of where to find the Spring Data named queries properties file. Will default to
   * {@code META-INF/jdbc-named-queries.properties}.
   */
  String namedQueriesLocation() default "";

  /**
   * Returns the {@link FactoryBean} class to be used for each repository instance. Defaults to
   * {@link DinoJdbcRepositoryFactoryBean}.
   */
  Class<?> repositoryFactoryBeanClass() default DinoJdbcRepositoryFactoryBean.class;

  /**
   * Configure the repository base class to be used to create repository proxies for this particular configuration.
   * {@link DinoJdbcRepositoryBase}.
   */
  Class<?> repositoryBaseClass() default DinoJdbcRepositoryBase.class;

  /**
   * Configures whether nested repository-interfaces (e.g. defined as inner classes) should be discovered by the
   * repositories infrastructure.
   */
  boolean considerNestedRepositories() default false;

  /**
   * Configures the name of the {@link org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations} bean
   * definition to be used to create repositories discovered through this annotation. Defaults to
   * {@code namedParameterJdbcTemplate}.
   */
  String jdbcOperationsRef() default "";

  /**
   * Configures the name of the {@link org.springframework.data.jdbc.core.convert.DataAccessStrategy} bean definition to
   * be used to create repositories discovered through this annotation. Defaults to {@code defaultDataAccessStrategy}.
   */
  String dataAccessStrategyRef() default "";

  /**
   * Configures the name of the {@link DataSourceTransactionManager} bean definition to be used to create repositories
   * discovered through this annotation. Defaults to {@code transactionManager}.
   *
   * @since 2.1
   */
  String transactionManagerRef() default "transactionManager";

  /**
   * Returns the key of the {@link QueryLookupStrategy} to be used for lookup queries for query methods. Defaults to
   * {@link QueryLookupStrategy.Key#CREATE_IF_NOT_FOUND}.
   *
   * @since 2.1
   */
  QueryLookupStrategy.Key queryLookupStrategy() default QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;
}
