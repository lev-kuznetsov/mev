package edu.dfci.cccb.mev.annotation.server.configuration;

import lombok.extern.log4j.Log4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import edu.dfci.cccb.mev.dataset.client.contract.AnnotatedClassViewRegistrar;
import edu.dfci.cccb.mev.dataset.client.contract.JavascriptInjectorRegistry;
import edu.dfci.cccb.mev.dataset.client.prototype.MevClientConfigurerAdapter;

@Configuration
@Log4j
public class AnnotationClientConfiguration extends MevClientConfigurerAdapter {

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.client.prototype.MevClientConfigurerAdapter#
   * registerAnnotatedClassViews
   * (edu.dfci.cccb.mev.dataset.client.contract.AnnotatedClassViewRegistrar) */
  @Override
  public void registerAnnotatedClassViews (AnnotatedClassViewRegistrar annotatedClassViewRegistrar) {
    annotatedClassViewRegistrar.register (Views.class);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.client.prototype.MevClientConfigurerAdapter#
   * registerJavascriptInjectors
   * (edu.dfci.cccb.mev.dataset.client.contract.JavascriptInjectorRegistry) */
  @Override
  public void registerJavascriptInjectors (JavascriptInjectorRegistry registry) {}

  /* (non-Javadoc)
   * @see
   * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
   * #addResourceHandlers
   * (org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry) */
  @Override
  public void addResourceHandlers (ResourceHandlerRegistry registry) {
    log.debug ("Adding resource handlers for annotation plugin");
    registry.addResourceHandler ("/annotation/javascript/**")
            .addResourceLocations ("classpath:/edu/dfci/cccb/mev/annotation/client/javascript/");
    registry.addResourceHandler ("/annotation/style/**")
            .addResourceLocations ("classpath:/edu/dfci/cccb/mev/annotation/client/style/");
  }
}
