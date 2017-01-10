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

import static edu.dfci.cccb.mev.dataset.Buffer.buffer;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * Dataset values
 * 
 * @author levk
 */
public class Values {

  /**
   * Dimensions
   */
  private final Map <String, Dimension> dimensions;
  /**
   * Values
   */
  private final List <Buffer> values;
  /**
   * Buffer size
   */
  private final int chunk;

  /**
   * @param dimensions
   * @param buffers
   * @param chunk
   */
  public Values (Map <String, Dimension> dimensions, List <Buffer> buffers, int chunk) {
    this.dimensions = dimensions;
    values = buffers;
    this.chunk = chunk;
  }

  /**
   * @param query
   *          map of dimension name to dimension key list
   * @return subset
   */
  @Path ("query")
  @POST
  @Transactional
  public Map <String, Map <String, Double>> query (Map <String, List <String>> query) {
    Dimension column = dimensions.get ("column"), row = dimensions.get ("row");
    return query.get ("row").stream ().collect (toMap (r -> r, r -> {
      return query.get ("column").stream ().collect (toMap (c -> c, c -> {
        long p = 8 * (row.indexOf (r) * column.size () + column.indexOf (c));
        return values.get ((int) (p / chunk)).buffer ().getDouble ((int) (p % chunk));
      }));
    }));
  }

  /**
   * @param values
   */
  @Transactional
  @PUT
  public void bind (Map <String, Map <String, Double>> values) {
    // FIXME if the annotations property is before the values property in a full
    // dataset JSON the annotations get wiped here
    Dimension row = dimensions.compute ("row", (x, y) -> new Dimension ()),
        column = dimensions.compute ("column", (x, y) -> new Dimension ());
    this.values.clear ();
    values.forEach ( (r, e) -> {
      row.keys.add (r);
      boolean f = column.size () == 0;
      e.forEach ( (c, v) -> {
        if (f) column.keys.add (c);
        long p = 8 * (row.indexOf (r) * column.size () + column.indexOf (c));
        int b = (int) (p / chunk);
        for (; b >= this.values.size (); this.values.add (buffer (new byte[chunk])));
        this.values.get (b).buffer ().putDouble ((int) (p % chunk), v);
      });
    });
  }
}
