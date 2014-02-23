package edu.dfci.cccb.mev.annotation.domain.probe.factory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsFactory;
import edu.dfci.cccb.mev.annotation.domain.probe.h2.H2ProbeAnnotations;

public class DBProbeAnnotationsFactory implements ProbeAnnotationsFactory {

  @Getter @Setter @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  
  public DBProbeAnnotationsFactory () {}

  @Override
  @SneakyThrows
  public ProbeAnnotations create (String platformId) {
    return new H2ProbeAnnotations (platformId, dataSource);
  }

}
