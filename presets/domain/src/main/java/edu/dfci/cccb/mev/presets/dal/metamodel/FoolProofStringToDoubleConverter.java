package edu.dfci.cccb.mev.presets.dal.metamodel;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;

import org.eobjects.metamodel.convert.StringToDoubleConverter;

public class FoolProofStringToDoubleConverter extends StringToDoubleConverter {

  @Override
  public Double toVirtualValue(String physicalValue) {
    if (physicalValue == null || physicalValue.length() == 0) {
      return null;
    }
    
    double value=NaN;
    if ("Inf".equalsIgnoreCase (physicalValue))
      value = POSITIVE_INFINITY;
    else if ("-Inf".equalsIgnoreCase (physicalValue))
      value = NEGATIVE_INFINITY;
    else if ("NA".equalsIgnoreCase (physicalValue))
      value = NaN;
    else if ("null".equalsIgnoreCase (physicalValue))
      value = NaN;
    else if (physicalValue==null)
      value = NaN;
    else if (physicalValue.length() == 0)
      value = NaN;
    else
      value = Double.parseDouble (physicalValue);
    
    return value;
   
  }
  
}
