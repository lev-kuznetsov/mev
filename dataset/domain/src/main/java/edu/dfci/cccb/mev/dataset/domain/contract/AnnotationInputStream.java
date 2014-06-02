package edu.dfci.cccb.mev.dataset.domain.contract;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface AnnotationInputStream extends Annotation {
  public InputStream getInputStream() throws IOException;
  public URL getUrl();
}
