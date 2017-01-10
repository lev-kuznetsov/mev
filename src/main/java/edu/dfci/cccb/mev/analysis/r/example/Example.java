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
package edu.dfci.cccb.mev.analysis.r.example;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.analysis.Analysis;
import edu.dfci.cccb.mev.analysis.Define;
import edu.dfci.cccb.mev.analysis.Resolve;
import edu.dfci.cccb.mev.analysis.r.R;
import edu.dfci.cccb.mev.analysis.r.Rserve;

/**
 * Example
 * 
 * @author levk
 */
@R ("c <- b + a")
@Entity
@EntityListeners (Rserve.class)
public class Example extends Analysis {

  private @Define @Column @Basic int a;
  private @Define @Column @Basic int b = 1;
  private @Resolve @Column @Basic int c;

  @PUT
  @Path ("a")
  @JsonProperty (required = false)
  public void a (int a) {
    this.a = a;
  }

  @PUT
  @Path ("b")
  @JsonProperty (required = false)
  public void b (int b) {
    this.b = b;
  }

  @GET
  @Path ("c")
  public int c () {
    return c;
  }
}
