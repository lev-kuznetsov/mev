package edu.dfci.cccb.mev.goeds.domain.dataset.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import edu.dfci.cccb.mev.dataset.domain.contract.AnnotationInputStream;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;

@RequiredArgsConstructor
public class GeoAnnotationStream implements AnnotationInputStream {
  
  private final @Getter RawInput content;
  private final @Getter URL url;
  
  @Override
  public InputStream getInputStream () throws IOException {
    return content.input ();
  }
  

}
