package edu.dfci.cccb.mev.annotation.domain.probe.contract;

import java.util.List;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.ProbeAnnotationSourceNotFoundException;

public interface ProbeAnnotationPlatforms {
  List<ProbeAnnotationPlatform> getAll();
  ProbeAnnotationPlatform get(String id) throws ProbeAnnotationSourceNotFoundException;
}
