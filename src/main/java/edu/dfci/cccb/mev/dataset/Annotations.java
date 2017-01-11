/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Dana-Farber Cancer Institute
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package edu.dfci.cccb.mev.dataset;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonValue;

import io.fabric8.annotations.Path;

/**
 * Annotations
 * 
 * @author levk
 */
public class Annotations {

  /**
   * Table
   */
  private final String index;
  /**
   * ElasticSearch
   */
//  TransportClient es;
  /**
   * Insert cache
   */
  final Map <String, Map <String, String>> cache = new HashMap <> ();

  /**
   * @param dimension
   */
  Annotations (Dimension dimension) {
    index = valueOf (dimension.id);
  }

  /**
   * @param annotations
   */
  @POST
  public void add (Map <String, Map <String, String>> annotations) {
    annotations.forEach ( (k, p) -> add (k, p));
  }

  /**
   * @param key
   * @param properties
   */
  @JsonAnySetter
  public void add (String key, Map <String, String> properties) {
    cache.computeIfAbsent (key, x -> new HashMap <> ()).putAll (properties);
  }

  /**
   * @param spec
   * @return matches
   */
  @Path ("query")
  @POST
  public Map <String, Map <String, ?>> search (String spec) {
//    SearchHits s = es.prepareSearch (index).setQuery (wrapperQuery (spec)).get ().getHits ();
//    return stream (s.spliterator (), false).collect (toMap (SearchHit::getId, h -> {
//      return h.fields ().entrySet ().stream ().collect (toMap (e -> e.getKey (), e -> {
//        Map <String, Object> v = new HashMap <String, Object> ();
//        v.put ("name", e.getValue ().getName ());
//        v.put ("values", e.getValue ().getValues ());
//        return v;
//      }));
//    }));
    return new HashMap<> ();
  }

  /**
   * @return names property names
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Path ("names")
  @GET
  @JsonValue
  public List <String> names () throws InterruptedException, ExecutionException {
//    System.out.println (es.admin ().indices ().getMappings (new GetMappingsRequest ().indices (index)).get ().mappings ());

    // TODO: implement
    return new ArrayList <> ();
  }
}
