package edu.dfci.cccb.mev.annotation.domain.probe.factory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsFactory;
import edu.dfci.cccb.mev.annotation.domain.probe.h2.H2GeneAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.h2.H2ProbeAnnotations;
@Log4j
public class DBProbeAnnotationsFactory implements ProbeAnnotationsFactory {

  @Getter @Setter @Inject @Named("probe-annotations-datasource") DataSource dataSource;
  
  public DBProbeAnnotationsFactory () {}

  @Override
  @SneakyThrows
  public ProbeAnnotations create (String platformId, String type) {
    if(type.equalsIgnoreCase ("Level2")){
      log.debug("Using LEVEL2 Annotatinos");
      return new H2ProbeAnnotations (platformId, dataSource);
    }else{
      log.debug("Using LEVEL2 Annotatinos");
      return new H2GeneAnnotations (platformId, dataSource);
    }
  }

}
