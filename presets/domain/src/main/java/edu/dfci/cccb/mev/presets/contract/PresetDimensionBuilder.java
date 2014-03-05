package edu.dfci.cccb.mev.presets.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;

public interface PresetDimensionBuilder {
  Dimension build (Type type, PresetDescriptor descriptor);
  Dimension build (Type type, PresetDescriptor descriptor, Selection selection);
  Dimension buildColumns(PresetDescriptor descriptor);
  Dimension buildRows(PresetDescriptor descriptor);
}
