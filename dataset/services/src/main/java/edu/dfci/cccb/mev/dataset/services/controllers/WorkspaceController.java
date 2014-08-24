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

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.inject.servlet.RequestScoped;

import edu.dfci.cccb.mev.dataset.domain.Dataset;

/**
 * @author levk
 * @since BAYLIE
 */
@Path ("/dataset/")
@RequestScoped
public class WorkspaceController {

  private @Inject @Named (WORKSPACE) Map<String, Dataset<String, Double>> workspace;

  /**
   * @return list of datasets
   */
  @GET
  public String[] list () {
    return workspace.keySet ().toArray (new String[0]);
  }
}
