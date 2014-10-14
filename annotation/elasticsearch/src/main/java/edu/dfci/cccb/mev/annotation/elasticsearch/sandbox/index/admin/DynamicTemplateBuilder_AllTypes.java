package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DynamicTemplateBuilder_AllTypes implements DynamicTemplateBuilder {

  public DynamicTemplateBuilder_AllTypes () {
    // TODO Auto-generated constructor stub
  }

  @Override
  public List<Map<String, Object>> build () {
    List<Map<String, Object>> templates = new ArrayList<Map<String, Object>> ();
    
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
    
    Map<String, Object> fields = new LinkedHashMap<String, Object> ();
    mapping.put("fields", fields);
    Map<String, Object> num = new LinkedHashMap<String, Object> ();
    fields.put("num", num);
    num.put("type", "long");
    num.put("index", "not_analyzed");
    num.put("ignore_malformed", true);
    num.put ("norms", norms);
    
    Map<String, Object> date = new LinkedHashMap<String, Object> ();
    fields.put("date", date);
    date.put("type", "date");
    date.put("index", "not_analyzed");
    date.put("ignore_malformed", true);
    date.put ("norms", norms);
    
    return templates;
  }
}
