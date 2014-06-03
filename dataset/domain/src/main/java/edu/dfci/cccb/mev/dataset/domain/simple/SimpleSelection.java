package edu.dfci.cccb.mev.dataset.domain.simple;

import java.util.List;
import java.util.Properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractSelection;

@NoArgsConstructor
@RequiredArgsConstructor
@Accessors (fluent = true)
@ToString
public class SimpleSelection extends AbstractSelection {

  @NonNull @Getter private String name;
  @NonNull @Getter private Properties properties;
  @NonNull @Getter private List<String> keys;
}
