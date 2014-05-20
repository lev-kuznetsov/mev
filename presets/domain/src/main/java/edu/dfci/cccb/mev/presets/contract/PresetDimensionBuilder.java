package edu.dfci.cccb.mev.presets.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;

public interface PresetDimensionBuilder {
  Dimension build (Type type, PresetDescriptor descriptor) throws PresetException;
  Dimension build (Type type, PresetDescriptor descriptor, Selection selection) throws PresetException;
  Dimension buildColumns(PresetDescriptor descriptor) throws PresetException;
  Dimension buildRows(PresetDescriptor descriptor) throws PresetException;
}
