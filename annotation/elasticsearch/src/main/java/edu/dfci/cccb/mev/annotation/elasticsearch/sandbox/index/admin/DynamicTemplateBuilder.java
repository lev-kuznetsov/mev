package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin;

import java.util.List;
import java.util.Map;

public interface DynamicTemplateBuilder {

  public abstract List<Map<String, Object>> build ();

}