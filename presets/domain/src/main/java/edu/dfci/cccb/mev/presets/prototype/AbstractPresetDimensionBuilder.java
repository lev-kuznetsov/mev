package edu.dfci.cccb.mev.presets.prototype;

import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListSelections;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;



@Log4j
public abstract class AbstractPresetDimensionBuilder {

  public AbstractPresetDimensionBuilder () {
    super ();
  }

  public Dimension buildColumns (PresetDescriptor descriptor)throws PresetException {    
    return build(Dimension.Type.COLUMN, descriptor);
  }

  public Dimension buildRows (PresetDescriptor descriptor) throws PresetException {    
    return build(Dimension.Type.ROW, descriptor);
  }

  abstract protected Dimension build(Type type, PresetDescriptor descriptor) throws PresetException;
  abstract protected Dimension build(Type type, PresetDescriptor descriptor, Selection selection) throws PresetException;
  
}