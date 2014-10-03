package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

public class IndexAdminException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public IndexAdminException () {
  }

  public IndexAdminException (String message) {
    super (message);
  }

  public IndexAdminException (Throwable cause) {
    super (cause);
  }

  public IndexAdminException (String message, Throwable cause) {
    super (message, cause);
  }

  public IndexAdminException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }

}
