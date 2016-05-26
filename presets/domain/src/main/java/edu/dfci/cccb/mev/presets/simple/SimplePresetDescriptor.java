package edu.dfci.cccb.mev.presets.simple;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetDescriptor;

@Accessors(fluent=true)
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreType(value=true)
public class SimplePresetDescriptor extends AbstractPresetDescriptor {

  @Getter private final String name;
  @Getter private final URL dataUrl;
  @Getter private final URL columnUrl;
  @Getter private final URL rowUrl;
  @Getter private final URL dataRootUrl;
  @Getter private final String relativeDataPath;
  @Getter private URL columnSourceUrl;

  public SimplePresetDescriptor(String name, URL dataRoot, String specDataUrl, String specColumnUrl, URL annotationsRoot, String specRowUrl) throws PresetException {

    try {
      this.name=name;
      dataRootUrl = dataRoot;
      relativeDataPath=specDataUrl;
      dataUrl = new URL(dataRootUrl, specDataUrl);
      columnUrl = new URL(dataRootUrl, specColumnUrl);
      columnSourceUrl = null;
      rowUrl = new URL(annotationsRoot, specRowUrl);
    } catch (MalformedURLException e) {
      throw new PresetException ("Could not create PresetDescriptor with parameters: " + dataRoot + ";" + specColumnUrl + ";"+specColumnUrl, e);
    }

  }

  public SimplePresetDescriptor(String name, URL dataRoot, String specDataUrl, String specColumnUrl, URL annotationsRoot, String specRowUrl, String specColumnSourceUrl) throws PresetException {
    this(name, dataRoot, specDataUrl, specColumnUrl, annotationsRoot, specRowUrl);
    try {
      columnSourceUrl = new URL(dataRoot, specColumnSourceUrl);
    } catch (MalformedURLException e) {
      throw new PresetException ("Could not create PresetDescriptor with parameters: " + dataRoot + ";" + specColumnUrl + ";"+specColumnUrl, e);
    }

  }
}
