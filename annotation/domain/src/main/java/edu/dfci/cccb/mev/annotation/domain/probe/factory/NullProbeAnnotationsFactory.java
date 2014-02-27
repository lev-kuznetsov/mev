package edu.dfci.cccb.mev.annotation.domain.probe.factory;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationsFactory;

public class NullProbeAnnotationsFactory implements ProbeAnnotationsFactory {

  public NullProbeAnnotationsFactory () {
    // TODO Auto-generated constructor stub
  }

  @Override
  public ProbeAnnotations create (String platformId) {
    return null;
  }

}
