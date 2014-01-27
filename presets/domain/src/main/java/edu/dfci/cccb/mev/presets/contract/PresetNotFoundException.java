package edu.dfci.cccb.mev.presets.contract;

public class PresetNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;
  public PresetNotFoundException(String message){
    super(message);
  }
}
