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

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.fabric8.annotations.Path;

/**
 * Dataset dimension
 * 
 * @author levk
 */
@Entity
@EntityListeners (Index.class)
public class Dimension {

  /**
   * Identifier
   */
  @Id @GeneratedValue (strategy = IDENTITY) long id;
  /**
   * Dimension keys
   */
  @ElementCollection @OrderColumn (name = "index") List <String> keys = new ArrayList <> ();
  /**
   * Index cache
   */
  private @Transient Map <String, Integer> index = new HashMap <> ();
  /**
   * Annotations
   */
  @Transient Annotations annotations = new Annotations (this);

  /**
   * @param key
   * @return index of the key
   */
  public int indexOf (String key) {
    return index.computeIfAbsent (key, k -> {
      int i = keys.indexOf (k);
      if (i < 0) throw new NotFoundException ("No key " + key + " found");
      return i;
    });
  }

  /**
   * @return size
   */
  public int size () {
    return keys.size ();
  }

  /**
   * @return keys
   */
  @JsonProperty
  @GET
  public List <String> keys () {
    return keys;
  }

  /**
   * @return annotations
   */
  @Path ("annotations")
  public Annotations annotations () {
    return annotations;
  }

  /**
   * @param annotations
   */
  @JsonProperty (value = "annotations", required = false)
  public void annotate (Map <String, Map <String, String>> annotations) {
    this.annotations.add (annotations);
  }
}
