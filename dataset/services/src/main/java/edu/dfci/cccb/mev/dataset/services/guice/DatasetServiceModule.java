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

import static edu.dfci.cccb.mev.dataset.domain.Analysis.ANALYSIS;
import static edu.dfci.cccb.mev.dataset.domain.Dataset.DATASET;
import static edu.dfci.cccb.mev.dataset.domain.Dimension.DIMENSION;
import static edu.dfci.cccb.mev.dataset.domain.messages.DatasetTsvMessageHandler.TEXT_TSV_TYPE;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.SessionScoped;

import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;
import edu.dfci.cccb.mev.common.services.guice.MevServiceModule;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ContentNegotiationMapper;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder;
import edu.dfci.cccb.mev.dataset.domain.Analysis;
import edu.dfci.cccb.mev.dataset.domain.Dataset;
import edu.dfci.cccb.mev.dataset.domain.Dimension;
import edu.dfci.cccb.mev.dataset.domain.annotation.NameOf;
import edu.dfci.cccb.mev.dataset.domain.guice.DatasetModule;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter;
import edu.dfci.cccb.mev.dataset.domain.prototype.DatasetAdapter.Workspace;

/**
 * @author levk
 * @since CRYSTAL
 */
public class DatasetServiceModule extends MevServiceModule {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {
    super.configure (binder);

    binder.install (new DatasetModule ());

    binder.install (new SingletonModule () {

      /**
       * Provides the context dataset name
       */
      @Provides
      @NameOf (Dataset.class)
      @RequestScoped
      public String dataset (UriInfo uri) {
        return uri.getPathParameters ().getFirst (DATASET);
      }

      /**
       * Provides the context analysis name
       */
      @Provides
      @RequestScoped
      @NameOf (Analysis.class)
      public String analysis (UriInfo uri) {
        return uri.getPathParameters ().getFirst (ANALYSIS);
      }

      @Provides
      @RequestScoped
      @NameOf (Dimension.class)
      public String dimension (UriInfo uri) {
        return uri.getPathParameters ().getFirst (DIMENSION);
      }
    });
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceModule#configure(
   * edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder) */
  @Override
  public void configure (ResourceBinder binder) {
    binder.publish (Key.get (new TypeLiteral<Workspace<String, Double>> () {}))
          .toProvider (new Provider<Workspace<String, Double>> () {
            @Override
            public Workspace<String, Double> get () {
              return DatasetAdapter.workspace ();
            }
          }).in (SessionScoped.class);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.common.services.guice.MevServiceModule#configure(edu
   * .dfci.cccb.mev.common.services.guice.jaxrs.ContentNegotiationMapper) */
  @Override
  public void configure (ContentNegotiationMapper content) {
    content.map ("tsv", TEXT_TSV_TYPE);
  }
}
