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
  
  public SimplePresetDescriptor(String name, URL root, String specDataUrl, String specColumnUrl) throws PresetException {
    
    try {
      this.name=name;
      dataUrl = new URL(root, specDataUrl);
      columnUrl = new URL(root, specColumnUrl);
    } catch (MalformedURLException e) {
      throw new PresetException ("Could not create PresetDescriptor with parameters: " + root + ";" + specColumnUrl + ";"+specColumnUrl, e);
    }

  }
}
