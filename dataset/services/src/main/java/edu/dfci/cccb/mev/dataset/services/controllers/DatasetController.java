/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.dataset.services.controllers;

import static edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter.WORKSPACE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import edu.dfci.cccb.mev.dataset.domain.Dataset;

/**
 * @author levk
 * @since BAYLIE
 */
@Path ("/dataset/{dataset}")
public class DatasetController {

  public static final String TAB_SEPARATED_VALUES = "text/tab-separated-values";
  public static final MediaType TAB_SEPARATED_VALUES_TYPE = new MediaType ("text", "tab-separated-values");

  private @Inject @Named (WORKSPACE) Map<String, Dataset<String, Double>> workspace;

  /**
   * @return dataset from workspace
   */
  @GET
  public Dataset<String, Double> get (@PathParam ("dataset") String name) {
    return workspace.get (name);
  }

  /**
   * @param name of dataset to put in the workspace
   * @param input TSV conforming to standards set by the builder
   */
  @PUT
  @Consumes ({ TEXT_PLAIN, TAB_SEPARATED_VALUES })
  public void put (@PathParam ("dataset") String name, Dataset<String, Double> dataset) throws Exception {
    workspace.put (name, dataset);
  }
}
