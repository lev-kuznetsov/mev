package edu.dfci.cccb.mev.annotation.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ProbeAnnotationsPersistenceConfiguration.class, ProbeAnnotationsLoaderConfiguration.class})
public class ProbeAnnotationsConfigurationMain {
  public static final String MEV_PROBE_ANNOTATIONS_PROPERTY_PREFIX="mev.annotations.probe.";  
}
