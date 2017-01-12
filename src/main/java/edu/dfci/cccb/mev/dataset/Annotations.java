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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;

import io.fabric8.annotations.Path;
import io.searchbox.action.BulkableAction;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

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
  JestClient es;
  /**
   * Index cache
   */
  final List <BulkableAction <?>> bulk = new ArrayList <> ();

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
  public void add (String key, Map <String, String> properties) {
    bulk.add (new Index.Builder (properties).id (key).build ());
  }

  /**
   * @param spec
   * @return search result JSON object as returned by the ES
   * @throws IOException
   */
  @Path ("query")
  @POST
  public String search (String spec) throws IOException {
    SearchResult r = es.execute (new Search.Builder (spec).addIndex (index).build ());
    if (r.getResponseCode () > 299) throw new InternalServerErrorException (r.getErrorMessage ());
    else return r.getJsonString ();
  }
}
