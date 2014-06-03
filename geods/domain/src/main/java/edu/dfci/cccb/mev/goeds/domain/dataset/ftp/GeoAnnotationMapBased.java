package edu.dfci.cccb.mev.goeds.domain.dataset.ftp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import lombok.RequiredArgsConstructor;
import edu.dfci.cccb.mev.dataset.domain.contract.AnnotationInputStream;

@RequiredArgsConstructor
public class GeoAnnotationMapBased implements AnnotationInputStream {
    
  private final Map<String, String> mapContent;
  
  @Override
  public InputStream getInputStream () throws IOException {
    StringBuilder sb = new StringBuilder ();    
    sb.append ("ID\tTitle\n");    
    for(Entry<String, String> entry : mapContent.entrySet ()){
      sb.append (entry.getKey ()).append ("\t").append (entry.getValue ()).append ("\n");      
    }
    return new ByteArrayInputStream (sb.toString ().getBytes (Charset.defaultCharset()));
  }

  @Override
  public URL getUrl () {
    // TODO Auto-generated method stub
    return null;
  }

}
