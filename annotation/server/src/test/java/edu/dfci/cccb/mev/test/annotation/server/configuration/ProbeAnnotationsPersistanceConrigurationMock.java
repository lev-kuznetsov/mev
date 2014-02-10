package edu.dfci.cccb.mev.test.annotation.server.configuration;

import static edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration.MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX;
import static java.io.File.separator;
import static java.lang.System.getProperty;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsPersistanceConriguration;

@Configuration 
@Profile ("test")
@Import(ProbeAnnotationsPersistanceConriguration.class)
public class ProbeAnnotationsPersistanceConrigurationMock {

  @Inject
  private Environment environment;

  @Bean(name="probe-annotations-datasource", destroyMethod = "close")
  public DataSource dataSource () {
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
    return dataSource;
  }


}
