package org.dinospring.data.autoconfig;

import java.sql.DatabaseMetaData;
import java.util.Locale;

import com.botbrain.dino.sql.dialect.Dialect;
import com.botbrain.dino.sql.dialect.MysqlDialect;
import com.botbrain.dino.sql.dialect.PostgreSQLDialect;

import org.dinospring.commons.context.ContextHelper;
import org.dinospring.commons.context.DinoContext;
import org.dinospring.data.dao.impl.JdbcSelectExecutorImpl;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Order(1)
public class DinoDataAutoConfiguration implements ApplicationContextAware {

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ContextHelper.setApplicationContext(applicationContext);

  }

  @Bean
  @Primary
  @ConditionalOnMissingBean
  public Dialect dialect(JdbcOperations jdbcOperations) {

    return jdbcOperations.execute((ConnectionCallback<Dialect>) conn -> {
      DatabaseMetaData metaData = conn.getMetaData();

      String name = metaData.getDatabaseProductName().toLowerCase(Locale.ENGLISH);

      if (name.contains("mysql") || name.contains("mariadb")) {
        return new MysqlDialect();
      }
      if (name.contains("postgresql")) {
        return new PostgreSQLDialect();
      }

      log.warn("Couldn't determine DB Dialect for {}", name);
      return new Dialect.DEFAULT();
    });
  }

  @Bean
  @ConditionalOnMissingBean
  public ProjectionFactory projectionFactory() {
    return new SpelAwareProxyProjectionFactory();
  }

  @Bean
  @ConditionalOnMissingBean
  public ContextHelper contextHelper(DinoContext dinoContext) {
    ContextHelper.setDinoContext(dinoContext);

    return ContextHelper.INST;
  }
}
