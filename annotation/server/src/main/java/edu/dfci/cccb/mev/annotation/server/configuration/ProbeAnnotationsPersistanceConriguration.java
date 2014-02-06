package edu.dfci.cccb.mev.annotation.server.configuration;

import static edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration.MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX;
import static java.io.File.separator;
import static java.lang.System.getProperty;

import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.apache.commons.dbcp.BasicDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@Log4j
@Configuration
@ComponentScan(value = "edu.dfci.cccb.mev.annotation.probe")
@PropertySource("classpath:probe_annotations.properties" )
public class ProbeAnnotationsPersistanceConriguration {

  
  @Inject
  private Environment environment;  
  
  @Profile("prod")
  @Bean(name="probe-annotations-datasource", destroyMethod = "close")
  public DataSource dataSourceProd () {
    BasicDataSource dataSource = new BasicDataSource ();
    dataSource.setDriverClassName (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.driver.class", "org.h2.Driver"));
    dataSource.setUrl (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.url",
                                                "jdbc:h2:file:"
                                                        + getProperty ("java.io.tmpdir") + separator
                                                        + "mev-probe-annotations"
                                                        + ";QUERY_CACHE_SIZE=100000"
                                                        + ";CACHE_SIZE=1048576"));
    dataSource.setUsername (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.username", "sa"));
    dataSource.setPassword (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.password", ""));
    log.info ("***SPRING PROFILE=PROD");
    return dataSource;
  }

  @Profile(value={"test"})
  @Bean(name="probe-annotations-datasource", destroyMethod = "close")
  public DataSource dataSourceTest () {
    BasicDataSource dataSource = new BasicDataSource ();
    dataSource.setDriverClassName (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.driver.class", "org.h2.Driver"));
    dataSource.setUrl (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.url",
                                                "jdbc:h2:file:"
                                                        + getProperty ("java.io.tmpdir") + separator
                                                        + "mev-probe-annotations-test"
                                                        + ";QUERY_CACHE_SIZE=100000"
                                                        + ";CACHE_SIZE=1048576"));
    dataSource.setUsername (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.username", "sa"));
    dataSource.setPassword (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.password", ""));
    log.info ("***SPRING PROFILE=test");
    return dataSource;
  }

  @Profile(value={"dev"})
  @Bean(name="probe-annotations-datasource", destroyMethod = "close")
  public DataSource dataSourceDev () {
    BasicDataSource dataSource = new BasicDataSource ();
    dataSource.setDriverClassName (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.driver.class", "org.h2.Driver"));
    dataSource.setUrl (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.url",
                                                "jdbc:h2:file:"
                                                        + getProperty ("java.io.tmpdir") + separator
                                                        + "mev-probe-annotations-dev"
                                                        + ";QUERY_CACHE_SIZE=100000"
                                                        + ";CACHE_SIZE=1048576"));
    dataSource.setUsername (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.username", "sa"));
    dataSource.setPassword (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.password", ""));
    log.info ("***SPRING PROFILE=Dev");
    return dataSource;
  }

  
  @Bean
  @Inject
  public LocalSessionFactoryBean sessionFactory (@Named("probe-annotations-datasource") DataSource dataSource) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean ();
    sessionFactory.setDataSource (dataSource);
    sessionFactory.setPackagesToScan (environment.getProperty ("session.factory.scan.packages",
                                                               String[].class,
                                                               new String[] { "edu.dfci.cccb.mev" }));
    Properties hibernateProperties = new Properties ();
    hibernateProperties.setProperty ("hibernate.hbm2ddl.auto",
                                     environment.getProperty ("hibernate.hbm2ddl.auto",
                                                              "create-drop"));
    hibernateProperties.setProperty ("hibernate.dialect",
                                     environment.getProperty ("hibernate.dialect",
                                                              "org.hibernate.dialect.H2Dialect"));
    hibernateProperties.setProperty ("hibernate.ejb.naming_strategy",
                                     environment.getProperty ("hibernate.ejb.naming_strategy",
                                                              "org.hibernate.cfg.ImprovedNamingStrategy"));
    hibernateProperties.setProperty ("hibernate.connection.charSet",
                                     environment.getProperty ("hibernate.connection.charSet",
                                                              "UTF-8"));
    sessionFactory.setHibernateProperties (hibernateProperties);
    return sessionFactory;
  }
  
  @Bean(name="probe-annotations-lazyConnectionDataSourceProxy") @Inject
  public LazyConnectionDataSourceProxy lazyConnectionDataSource(@Named("probe-annotations-datasource") DataSource dataSource) {
      return new LazyConnectionDataSourceProxy(dataSource);
  }

  @Bean(name="probe-annotations-transactionAwareDataSourceProxy")  @Inject
  public TransactionAwareDataSourceProxy transactionAwareDataSource(@Named("probe-annotations-lazyConnectionDataSourceProxy") LazyConnectionDataSourceProxy lazyConnectionDataSource) {
      return new TransactionAwareDataSourceProxy(lazyConnectionDataSource);
  }

  @Bean   @Inject
  public DataSourceTransactionManager transactionManager(@Named("probe-annotations-lazyConnectionDataSourceProxy") LazyConnectionDataSourceProxy lazyConnectionDataSource) {
      return new DataSourceTransactionManager(lazyConnectionDataSource);
  }

  @Bean(name="probe-annotations-connectionProvider")  @Inject
  public DataSourceConnectionProvider connectionProvider(@Named("probe-annotations-transactionAwareDataSourceProxy") TransactionAwareDataSourceProxy transactionAwareDataSource) {
      return new DataSourceConnectionProvider(transactionAwareDataSource);
  }

  /*
  @Bean
  public JOOQToSpringExceptionTransformer jooqToSpringExceptionTransformer() {
      return new JOOQToSpringExceptionTransformer();
  }
*/
  
  @Bean(name="probe-annotations-defaultConfiguration")
  public DefaultConfiguration configuration(@Named ("probe-annotations-connectionProvider") DataSourceConnectionProvider connectionProvider) {
      DefaultConfiguration jooqConfiguration = new DefaultConfiguration();

      jooqConfiguration.set(connectionProvider);
//      jooqConfiguration.set(new DefaultExecuteListenerProvider(
//          jooqToSpringExceptionTransformer()
//      ));

      String sqlDialectName = environment.getRequiredProperty(MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"jooq.sql.dialect");
      SQLDialect dialect = SQLDialect.valueOf(sqlDialectName);
      jooqConfiguration.set(dialect);

      return jooqConfiguration;
  }

  @Bean
  public DefaultDSLContext dsl(@Named("probe-annotations-defaultConfiguration") DefaultConfiguration configuration) {
      return new DefaultDSLContext(configuration);
  }

  @Bean @Inject
  public DataSourceInitializer dataSourceInitializer(@Named("probe-annotations-datasource") DataSource dataSource) {
      DataSourceInitializer initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource);

      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
      populator.addScript(
              new ClassPathResource(environment.getRequiredProperty(MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"db.schema.script"))
      );

      initializer.setDatabasePopulator(populator);
      return initializer;
  }
}

