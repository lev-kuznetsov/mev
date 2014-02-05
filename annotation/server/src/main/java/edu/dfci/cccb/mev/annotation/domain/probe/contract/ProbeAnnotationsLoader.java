package edu.dfci.cccb.mev.annotation.domain.probe.contract;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public interface ProbeAnnotationsLoader {

  public abstract ProbeAnnotationsLoader init (URL rootFolder, String suffix, long modifiedInLastMillis) throws IOException,
                                                                                      URISyntaxException;

  public abstract void loadUrlResource (URL url) throws IOException;

}