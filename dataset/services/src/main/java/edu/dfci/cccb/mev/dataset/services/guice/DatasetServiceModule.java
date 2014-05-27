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

package edu.dfci.cccb.mev.dataset.services.guice;

import static edu.dfci.cccb.mev.dataset.domain.contract.annotation.Workspace.WORKSPACE;

import java.util.Map;

import javax.inject.Named;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.SessionScoped;

import edu.dfci.cccb.mev.common.services.guice.MevServiceModule;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;
import edu.dfci.cccb.mev.dataset.services.controllers.DatasetController;
import edu.dfci.cccb.mev.dataset.services.controllers.WorkspaceController;

/**
 * @author levk
 * @since CRYSTAL
 */
public class DatasetServiceModule implements Module {

  /**
   * Provides a workspace for holding datasets
   */
  @Provides
  @SessionScoped
  @Named (WORKSPACE)
  public Map<String, Dataset<String, Double>> workspace () {
    return DatasetAdapter.workspace ();
  }

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    binder.install (new MevServiceModule () {
      @Override
      public void configure (ResourceBinder binder) {
        binder.publish (WorkspaceController.class).in (RequestScoped.class);
        binder.publish (DatasetController.class).in (RequestScoped.class);
      }
    });
  }
}
