package edu.dfci.cccb.mev.presets.rest.configuration;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import edu.dfci.cccb.mev.configuration.util.archaius.ArchaiusConfig;
import edu.dfci.cccb.mev.configuration.util.contract.Config;

@Log4j
@Configuration
//@PropertySources({
//@PropertySource(value={"classpath:persistence/presets.persistence-defaults.properties"}),
//@PropertySource(value="classpath:persistence/presets.persistence-${spring_profiles_active}.properties", ignoreResourceNotFound=true)
//})
public class PresetPersistenceConfiguration {
  private final String MEV_PRESETS_PROPERTY_PREFIX = "mev.presets.";
  
  public PresetPersistenceConfiguration () {
    log.info ("***loading "+this.getClass ().getSimpleName ()+"****");
  }
  
//  @Inject private Environment environment;  
  @Inject @Named("presets-persistence-config") private Config environment;  
  
  
//  @Bean(name="presets-h2-server")
//  public Lifecycle presetsH2TcpServer(){
//    int port = environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"h2.tcp.port", Integer.class, 18054);
//    Lifecycle server = new H2Server("Presets", port, "-tcp","-tcpAllowOthers");    
//    return server;
//  }
//
//  @Bean
//  public Lifecycle presetsH2Console(){
//    int port = environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"h2.console.port", Integer.class, 18053);
//    int fetchSize = environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"h2.serverResultSetFetchSize", Integer.class, 1000);
//    Lifecycle server = new H2Console("Presets", port, "h2.serverResultSetFetchSize", String.valueOf(fetchSize));
//    server.start ();
//    return server;
//  }
//@Bean(name="presets-h2-server")
//public Lifecycle presetsH2TcpServer(){
//  int port = environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"h2.tcp.port", Integer.class, 18054);
//  Lifecycle server = new H2Server("Presets", port, "-tcp","-tcpAllowOthers");    
//  return server;
//}  
//  @Bean(name="presetDetasetBuilder", autowire=Autowire.NO)  
//  protected DatasetBuilder presetDatasetBuilder(){
//    SimpleDatasetBuilder datasetBuilder = new SimpleDatasetBuilder();    
//    return datasetBuilder;
//  }
//  
//  @Bean 
//  public PresetValuesLoader presestValuesLoader(@Named ("presets-datasource") DataSource dataSource){
//    return new SimplePresetValuesLoader (presetValueStoreBuilderFactory(dataSource), presetDatasetBuilder());
//  }
  
//  @Bean
//  public PresetDimensionBuilder presetDimensionBuilder(@Named("presets-jooq-context") DSLContext context){
//    return new PresetDimensionBuilderFlatTable (context);
//  }
  
//  @Bean(name="presets-dataset-builder") @Inject 
//  public PresetDatasetBuilder presetDatasetBuilder(@Named("presets-datasource") DataSource dataSource, 
//                                                   @Named("presets-jooq-context") DSLContext context,
//                                                   PresetDimensionBuilder dimensionBuilder) throws SQLException{
//    log.debug ("***PresetDataSetBuilder: FLATTABLE-DB");
//    return new PresetDatasetBuilderFlatTableDB (dataSource, context, dimensionBuilder);
//  }

  
  @Bean(name="presets-datasource", destroyMethod = "close")
  public DataSource dataSource () {
    
    BasicDataSource dataSource = new BasicDataSource ();
    dataSource.setDriverClassName (environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"database.driver.class", "org.h2.Driver"));
    dataSource.setUrl (environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"database.url",
                                                "jdbc:h2:file:"
                                                        + getProperty ("java.io.tmpdir") + "/"
                                                        + "mev-presets"
                                                        + ";QUERY_CACHE_SIZE=100000"
                                                        + ";CACHE_SIZE=1048576"));
    dataSource.setUsername (environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"database.username", "sa"));
    dataSource.setPassword (environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"database.password", ""));
    
    log.info ("*** presets-datasource config: " + dataSource.getUrl ());    
  
    return dataSource;
  }
  
  
  @Bean(name="presets-session-factory") @Inject
  public LocalSessionFactoryBean sessionFactory (@Named("presets-datasource") DataSource datasource) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean ();
    sessionFactory.setDataSource (datasource);
    sessionFactory.setPackagesToScan (environment.getStringArray (MEV_PRESETS_PROPERTY_PREFIX+"session.factory.scan.packages",
            "[edu.dfci.cccb.mev]" ));
    
    Properties hibernateProperties = new Properties ();
    hibernateProperties.setProperty ("hibernate.hbm2ddl.auto",
                                     environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"hibernate.hbm2ddl.auto",
                                                              "create-drop"));
    hibernateProperties.setProperty ("hibernate.dialect",
                                     environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"hibernate.dialect",
                                                              "org.hibernate.dialect.H2Dialect"));
    hibernateProperties.setProperty ("hibernate.ejb.naming_strategy",
                                     environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"hibernate.ejb.naming_strategy",
                                                              "org.hibernate.cfg.ImprovedNamingStrategy"));
    hibernateProperties.setProperty ("hibernate.connection.charSet",
                                     environment.getProperty (MEV_PRESETS_PROPERTY_PREFIX+"hibernate.connection.charSet",
                                                              "UTF-8"));
    sessionFactory.setHibernateProperties (hibernateProperties);
    return sessionFactory;
  }
  
  @Bean(name="presets-lazy-connection-datasource") @Inject
  public LazyConnectionDataSourceProxy lazyConnectionDataSource(@Named("presets-datasource") DataSource datasource) {
      return new LazyConnectionDataSourceProxy(datasource);
  }
  
  @Bean(name="presets-transaction-aware-datasrouce") @Inject
  public TransactionAwareDataSourceProxy transactionAwareDataSource(@Named("presets-lazy-connection-datasource") LazyConnectionDataSourceProxy lazyProxy) {
      return new TransactionAwareDataSourceProxy(lazyProxy);
  }
  
  @Bean
  public DataSourceTransactionManager transactionManager(@Named("presets-datasource") DataSource datasource) {
      return new DataSourceTransactionManager(datasource);
  }
  
  @Bean(name="presets-connection-provider") @Inject
  public DataSourceConnectionProvider connectionProvider(@Named("presets-transaction-aware-datasrouce") TransactionAwareDataSourceProxy proxy) {
      return new DataSourceConnectionProvider(proxy);
  }
  
  /*
  @Bean
  public JOOQToSpringExceptionTransformer jooqToSpringExceptionTransformer() {
      return new JOOQToSpringExceptionTransformer();
  }
  */
  
  @Bean(name="presets-jooq-configuration") @Inject
  public DefaultConfiguration configuration(@Named("presets-connection-provider") DataSourceConnectionProvider provider) {
      DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
  
      jooqConfiguration.set(provider);
  //    jooqConfiguration.set(new DefaultExecuteListenerProvider(
  //        jooqToSpringExceptionTransformer()
  //    ));
  
      String sqlDialectName = environment.getProperty(MEV_PRESETS_PROPERTY_PREFIX+"jooq.sql.dialect");
      SQLDialect dialect = SQLDialect.valueOf(sqlDialectName);
      jooqConfiguration.set(dialect);
  
      return jooqConfiguration;
  }
  
  @Bean(name="presets-jooq-context") @Inject
  public DefaultDSLContext dsl(@Named("presets-jooq-configuration") DefaultConfiguration config) {
      return new DefaultDSLContext(config);
  }
  
}
