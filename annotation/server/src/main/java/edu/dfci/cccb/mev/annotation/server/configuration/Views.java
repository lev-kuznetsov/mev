package edu.dfci.cccb.mev.annotation.server.configuration;

import static edu.dfci.cccb.mev.api.client.support.view.ViewBuilders.freemarker;
import static freemarker.ext.beans.BeansWrapper.getDefaultInstance;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import freemarker.template.TemplateModelException;

public class Views {

  @Bean
  public FreeMarkerView annotations () throws TemplateModelException {
    FreeMarkerView view = freemarker ().url ("/edu/dfci/cccb/mev/web/views/annotations.ftl").build ();
    view.addStaticAttribute ("Dimension$Type",
                             getDefaultInstance ().getEnumModels ().get (Type.class.getName ()));
    return view;
  }
}
