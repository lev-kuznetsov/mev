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

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.workspace.Item;

/**
 * Dataset
 * 
 * @author levk
 */
@Entity
public class Dataset extends Item {

  /**
   * Default chunk size
   */
  private static final int DEFAULT_CHUNK_SIZE = 1 << 18; // Less than 1<<31

  /**
   * Dimensions
   */
  private @OneToMany (cascade = ALL) Map <String, Dimension> dimensions = new HashMap <> ();
  /**
   * Chunk size
   */
  private @Column @Basic int chunk = DEFAULT_CHUNK_SIZE;
  /**
   * Value buffers
   */
  private @OneToMany (cascade = ALL) @OrderColumn List <Buffer> values = new ArrayList <> ();

  /**
   * @return values
   */
  @Path ("values")
  @JsonIgnore
  public synchronized Values values () {
    return new Values (dimensions, values, chunk);
  }

  /**
   * @param values
   */
  @JsonProperty (value = "values", required = false)
  public void bind (Map <String, Map <String, Double>> values) {
    values ().bind (values);
  }

  /**
   * @param annotations
   */
  @JsonProperty (value = "annotations", required = false)
  public void annotate (Map <String, Map <String, Map <String, String>>> annotations) {
    annotations.forEach ( (d, a) -> dimension (d).annotate (a));
  }

  /**
   * @param dimension
   *          name
   * @return dimension
   */
  @Path ("{dimension}")
  @JsonIgnore
  public Dimension dimension (@PathParam ("dimension") String dimension) {
    return dimensions.get (dimension);
  }

  /**
   * @return dimension names
   */
  @GET
  public Map <String, Dimension> dimensions () {
    return dimensions;
  }
}
