package edu.dfci.cccb.mev.annotation.domain.probe.contract;

import java.util.List;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.ProbeAnnotationSourceNotFoundException;

public interface ProbeAnnotationSources {
  List<ProbeAnnotationSource> getAll();
  ProbeAnnotationSource get(String name) throws ProbeAnnotationSourceNotFoundException;
}
