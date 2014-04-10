package edu.dfci.cccb.mev.anova.domain.contract;

import java.util.List;
import java.util.Map;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;

public interface Anova extends Analysis {
  
  public interface Entry {
    interface Pairing{
      String partnerA();
      String partnerB();
    }
    String geneId ();
    double pValue ();
    Map<Pairing, Double> logFoldChanges();
  }
  Iterable<Entry> fullResults ();
  List<Entry.Pairing> logFoldChangePairings();
}
