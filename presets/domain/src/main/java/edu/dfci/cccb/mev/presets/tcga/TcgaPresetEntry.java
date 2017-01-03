package edu.dfci.cccb.mev.presets.tcga;

/**
 * Created by antony on 12/14/16.
 */
public interface TcgaPresetEntry {
    Object[] formatPreset();
    boolean isValid();
}
