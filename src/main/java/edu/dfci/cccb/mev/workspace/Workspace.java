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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Workspace
 * 
 * @author levk
 */
@Entity
public class Workspace {

  /**
   * Identifier
   */
  protected @Id @GeneratedValue (strategy = IDENTITY) long id;
  /**
   * Projects
   */
  private @ManyToMany (cascade = ALL) Map <String, Project> projects = new HashMap <> ();

  /**
   * @param name
   * @return project
   */
  @Path ("{project}")
  @Transactional
  public Project project (@PathParam ("project") String name) {
    return projects.getOrDefault (name, new Project () {

      /*
       * (non-Javadoc)
       * 
       * @see edu.dfci.cccb.mev.workspace.Project#items()
       */
      @Override
      @GET
      @DELETE
      public Map <String, Item> items () {
        throw new NotFoundException ("No project " + name + " found");
      }

      /*
       * (non-Javadoc)
       * 
       * @see edu.dfci.cccb.mev.workspace.Project#add(java.util.Map)
       */
      @Override
      @PUT
      @POST
      public void add (Map <String, Item> items) {
        Project p = new Project ();
        p.add (items);
        projects.put (name, p);
      }

      /*
       * (non-Javadoc)
       * 
       * @see edu.dfci.cccb.mev.workspace.Project#resource(java.lang.String)
       */
      @Override
      public Object resource (String x) {
        throw new NotFoundException ("No project " + name + " found");
      }
    });
  }

  /**
   * @param projects
   *          to add
   */
  @POST
  @Transactional
  public void add (Map <String, Project> projects) {
    this.projects.putAll (projects);
  }

  /**
   * @param workspace
   *          to set
   */
  @PUT
  @Transactional
  public void set (Map <String, Project> workspace) {
    projects.clear ();
    add (workspace);
  }

  /**
   * @return project names
   */
  @GET
  @Transactional
  public Collection <String> projects () {
    return projects.keySet ();
  }
}
