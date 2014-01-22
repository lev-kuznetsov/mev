package edu.dfci.cccb.mev.web.configuration.container;

import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;

public class MevRequestToViewNameTranslator extends DefaultRequestToViewNameTranslator {
  @Override
  protected String transformPath (String lookupPath) {
   
    String transformedPath = super.transformPath (lookupPath);
    if(transformedPath.contains ("container/view/elements/")){
      transformedPath = transformedPath.replace ("container/view/", "");
    }
    return transformedPath;
  }
}
