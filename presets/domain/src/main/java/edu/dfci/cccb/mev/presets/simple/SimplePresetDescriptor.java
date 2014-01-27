package edu.dfci.cccb.mev.presets.simple;

import java.net.URL;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetDescriptor;

@Accessors(fluent=true)
@RequiredArgsConstructor
public class SimplePresetDescriptor extends AbstractPresetDescriptor {

  @Getter private final URL dataUrl;
  @Getter private final URL columnUrl;
  
}
