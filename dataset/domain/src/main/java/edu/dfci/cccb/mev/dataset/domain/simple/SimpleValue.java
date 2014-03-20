package edu.dfci.cccb.mev.dataset.domain.simple;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValue;

@RequiredArgsConstructor
@Accessors(fluent=true)
public class SimpleValue extends AbstractValue {

  private final @Getter String row;
  private final @Getter String column;
  private final @Getter double value;  

}
