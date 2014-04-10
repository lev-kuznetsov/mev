package edu.dfci.cccb.mev.anova.domain.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.anova.domain.contract.Anova.Entry;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Accessors (fluent = true)
public class SimplePairing implements Entry.Pairing{

  private @Getter final String partnerA;
  private @Getter final String partnerB;



}
