package edu.dfci.cccb.mev.annotation.server.configuration;

import static edu.dfci.cccb.mev.api.client.support.view.ViewBuilders.freemarker;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class Views {

  /*
	@Bean
	  public FreeMarkerView annotations () {
	    return freemarker ().url ("/edu/dfci/cccb/mev/annotation/client/views/annotations.ftl").build ();
	  }
	*/
	@Bean
  public FreeMarkerView annotations () throws TemplateModelException {
	  
    FreeMarkerView view = freemarker ().url ("/edu/dfci/cccb/mev/web/views/annotations.ftl").build ();
    
    
    TemplateHashModel enumModels = BeansWrapper.getDefaultInstance().getEnumModels();        
    TemplateHashModel roundingModeEnums = (TemplateHashModel) enumModels.get("java.math.RoundingMode");
    TemplateHashModel dimensionTypeEnums = (TemplateHashModel) enumModels.get("edu.dfci.cccb.mev.dataset.domain.contract.Dimension$Type");
    
    view.addStaticAttribute("Dimension$Type", dimensionTypeEnums);
    return view;
    
  }
}
