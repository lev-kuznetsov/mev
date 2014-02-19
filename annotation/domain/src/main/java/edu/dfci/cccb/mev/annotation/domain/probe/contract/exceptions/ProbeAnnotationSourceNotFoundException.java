package edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions;

public class ProbeAnnotationSourceNotFoundException extends AnnotationException {

  private static final long serialVersionUID = 1L;

  public ProbeAnnotationSourceNotFoundException () {   }

  public ProbeAnnotationSourceNotFoundException (String message) {
    super (message);
  }

  public ProbeAnnotationSourceNotFoundException (Throwable cause) {
    super (cause);
  }

  public ProbeAnnotationSourceNotFoundException (String message, Throwable cause) {
    super (message, cause);
  }

  public ProbeAnnotationSourceNotFoundException (String message,
                                           Throwable cause,
                                           boolean enableSuppression,
                                           boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }

}
