package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DynamicTemplateBuilder_NotAnalyzed implements DynamicTemplateBuilder {
  
  public DynamicTemplateBuilder_NotAnalyzed () {
    
  }
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder#build()
   */
  @Override
  public List<Map> build(){
    
    ArrayList<Map> templates = new ArrayList<Map> ();
    
    Map<String, Object> templateWrapper = new LinkedHashMap<String, Object> ();    
    templates.add(templateWrapper);
    
    Map<String, Object> template = new LinkedHashMap<String, Object> ();
    templateWrapper.put ("dont_analyze", template);
    template.put ("match", "*");
    
    Map<String, Object> mapping = new LinkedHashMap<String, Object> ();
    template.put ("mapping", mapping);
    mapping.put ("type", "string");
    mapping.put ("index", "not_analyzed");
    mapping.put ("ignore_malformed", true);
    Map<String, Object> norms = new LinkedHashMap<String, Object> ();
    mapping.put ("norms", norms);
    norms.put ("enabled", false);
    
    return templates;
  }
}