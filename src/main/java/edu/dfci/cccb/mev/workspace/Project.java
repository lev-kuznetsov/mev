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
package edu.dfci.cccb.mev.workspace;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Project
 * 
 * @author levk
 */
@Entity
public class Project {

  /**
   * Identifier
   */
  private @Id @GeneratedValue (strategy = IDENTITY) long id;
  /**
   * Items
   */
  private @OneToMany (cascade = ALL) Map <String, Item> items = new HashMap <> ();

  /**
   * @return items
   */
  @JsonValue
  @GET
  public Map <String, Item> items () {
    return items;
  }

  /**
   * @param items
   */
  @POST
  public void add (Map <String, Item> items) {
    this.items.putAll (items);
  }

  /**
   * @param name
   * @param item
   */
  @JsonAnySetter
  public void add (String name, Item item) {
    items.put (name, item);
  }

  /**
   * @param name
   * @return resource
   */
  @Path ("{item}")
  public Object resource (@PathParam ("item") String name) {
    return items.getOrDefault (name, new Item () {
      @PUT
      public void set (Item item) {
        items.put (name, item);
      }

      @GET
      public void notFound () {
        throw new NotFoundException ("No item " + name + " found");
      }

      @Override
      public boolean equals (Object obj) {
        return false;
      }

      @Override
      public int hashCode () {
        return 0;
      }
    });
  }
}
