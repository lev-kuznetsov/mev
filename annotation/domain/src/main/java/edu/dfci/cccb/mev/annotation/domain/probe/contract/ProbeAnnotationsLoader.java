package edu.dfci.cccb.mev.annotation.domain.probe.contract;

import java.net.URL;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.AnnotationException;

public interface ProbeAnnotationsLoader {

  public abstract int loadAll (URL rootFolder, String suffix) throws AnnotationException;
  public abstract int loadAll (ProbeAnnotationSources probeAnnotationSources) throws AnnotationException;

  public abstract void loadUrlResource (URL url) throws AnnotationException;

}