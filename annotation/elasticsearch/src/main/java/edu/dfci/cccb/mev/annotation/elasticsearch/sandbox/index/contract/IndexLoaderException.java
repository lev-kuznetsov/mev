package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.contract;

public class IndexLoaderException extends Exception {

  public IndexLoaderException () {
  }

  public IndexLoaderException (String message) {
    super (message);
  }

  public IndexLoaderException (Throwable cause) {
    super (cause);
  }

  public IndexLoaderException (String message, Throwable cause) {
    super (message, cause);
  }

  public IndexLoaderException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super (message, cause, enableSuppression, writableStackTrace);
  }

}
