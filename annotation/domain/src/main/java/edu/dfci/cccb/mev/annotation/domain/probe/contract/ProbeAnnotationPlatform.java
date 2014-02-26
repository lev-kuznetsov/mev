package edu.dfci.cccb.mev.annotation.domain.probe.contract;

import java.net.URL;


public interface ProbeAnnotationPlatform {
  String id();
  String name();
  ProbeAnnotations annotations();
  URL annotationsUrl();
}
