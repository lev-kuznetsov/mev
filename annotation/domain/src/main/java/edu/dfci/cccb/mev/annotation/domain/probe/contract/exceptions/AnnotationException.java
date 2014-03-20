package edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions;

import edu.dfci.cccb.mev.dataset.domain.contract.MevException;

public class AnnotationException extends MevException {
  
  private static final long serialVersionUID = 537722865660391731L;
  
  public AnnotationException () {
  }

  public AnnotationException (String message) {
    super (message);
  }

  public AnnotationException (Throwable cause) {
    super (cause);
  }

  public AnnotationException (String message, Throwable cause) {
    super (message, cause);    
  }

  public AnnotationException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }

}
