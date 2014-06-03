package edu.dfci.cccb.mev.goeds.domain.contract;

import java.net.URL;
import java.util.Map;

public interface GeoSource {
  public URL getPlatformUrl();
  public URL getDatasetUrl();
  public Map<String, String> getSamples();
}
