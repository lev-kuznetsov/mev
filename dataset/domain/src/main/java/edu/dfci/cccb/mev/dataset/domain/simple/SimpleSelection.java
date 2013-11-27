package edu.dfci.cccb.mev.dataset.domain.simple;

import java.util.List;
import java.util.Properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractSelection;

@RequiredArgsConstructor
@Accessors(fluent=true)
public class SimpleSelection extends AbstractSelection {

  @NonNull @Getter private String name;
  @NonNull @Getter private final Properties properties;
  @NonNull @Getter private List<String> keys;
  
  @Override
  public Dataset export (String name) {
    // TODO Auto-generated method stub
    return null;
  }

}
