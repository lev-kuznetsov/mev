package edu.dfci.cccb.mev.annotation.server.configuration;


import static edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsConfigurationMain.MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX;
import static java.lang.System.getProperty;

import java.util.Properties;

import javax.inject.Inject;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.apache.commons.dbcp.BasicDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
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
@PropertySources({
@PropertySource(value={"classpath:persistence/probe_annotations.persistence-defaults.properties"}),
@PropertySource(value="classpath:persistence/probe_annotations.persistence-${spring_profiles_active}.properties", ignoreResourceNotFound=true)
})
public class ProbeAnnotationsPersistenceConfiguration {
  
  public ProbeAnnotationsPersistenceConfiguration () {
    log.info ("***loading "+this.getClass ().getSimpleName ()+"****");
  }
  
  @Inject
  private Environment environment;
 
  @Bean(name="probe-annotations-datasource", destroyMethod = "close")
  public DataSource dataSource () {
    BasicDataSource dataSource = new BasicDataSource ();
    dataSource.setDriverClassName (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.driver.class", "org.h2.Driver"));
    dataSource.setUrl (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.url",
                                                "jdbc:h2:file:"
                                                        + getProperty ("java.io.tmpdir") + "/"
                                                        + "mev-probe-annotations"
                                                        + ";QUERY_CACHE_SIZE=100000"
                                                        + ";CACHE_SIZE=1048576"));
    dataSource.setUsername (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.username", "sa"));
    dataSource.setPassword (environment.getProperty (MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"database.password", ""));
    
    log.info ("*** probe-annotations-datasource config: " + dataSource.getUrl ());    

    return dataSource;
  }
  

  @Bean
  public LocalSessionFactoryBean sessionFactory () {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean ();
    sessionFactory.setDataSource (dataSource());
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
  
  @Bean
  public LazyConnectionDataSourceProxy lazyConnectionDataSource() {
      return new LazyConnectionDataSourceProxy(dataSource());
  }

  @Bean
  public TransactionAwareDataSourceProxy transactionAwareDataSource() {
      return new TransactionAwareDataSourceProxy(lazyConnectionDataSource());
  }

  @Bean
  public DataSourceTransactionManager transactionManager() {
      return new DataSourceTransactionManager(lazyConnectionDataSource());
  }

  @Bean
  public DataSourceConnectionProvider connectionProvider() {
      return new DataSourceConnectionProvider(transactionAwareDataSource());
  }

  /*
  @Bean
  public JOOQToSpringExceptionTransformer jooqToSpringExceptionTransformer() {
      return new JOOQToSpringExceptionTransformer();
  }
*/
  
  @Bean
  public DefaultConfiguration configuration() {
      DefaultConfiguration jooqConfiguration = new DefaultConfiguration();

      jooqConfiguration.set(connectionProvider());
//      jooqConfiguration.set(new DefaultExecuteListenerProvider(
//          jooqToSpringExceptionTransformer()
//      ));

      String sqlDialectName = environment.getRequiredProperty(MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"jooq.sql.dialect");
      SQLDialect dialect = SQLDialect.valueOf(sqlDialectName);
      jooqConfiguration.set(dialect);

      return jooqConfiguration;
  }

  @Bean
  public DefaultDSLContext dsl() {
      return new DefaultDSLContext(configuration());
  }

  @Bean
  public DataSourceInitializer dataSourceInitializer() {
      DataSourceInitializer initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource());

      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
      populator.addScript(
              new ClassPathResource(environment.getRequiredProperty(MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX+"db.schema.script"))
      );

      initializer.setDatabasePopulator(populator);
      return initializer;
  }
  
  
}

