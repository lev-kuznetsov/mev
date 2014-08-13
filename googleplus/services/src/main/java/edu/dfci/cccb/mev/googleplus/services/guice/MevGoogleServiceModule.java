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

package edu.dfci.cccb.mev.googleplus.services.guice;

import static edu.dfci.cccb.mev.googleplus.domain.guice.ScopeBinder.Scopes.DRIVE;
import static edu.dfci.cccb.mev.googleplus.domain.guice.ScopeBinder.Scopes.EMAIL;
import static edu.dfci.cccb.mev.googleplus.domain.guice.ScopeBinder.Scopes.PROFILE;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.servlet.SessionScoped;

import edu.dfci.cccb.mev.common.services.guice.MevServiceModule;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder;
import edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule;
import edu.dfci.cccb.mev.googleplus.domain.guice.ScopeBinder;
import edu.dfci.cccb.mev.googleplus.domain.guice.ServicesScopeBindingBuilder;
import edu.dfci.cccb.mev.googleplus.services.controllers.AuthorizationController;

/**
 * @author levk
 * @since CRYSTAL
 */
public class MevGoogleServiceModule implements Module {

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public void configure (Binder binder) {

    binder.install (new GoogleDomainModule () {
      /* (non-Javadoc)
       * @see
       * edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule#configure
       * (edu.dfci.cccb.mev.googleplus.domain.guice.ScopeBinder) */
      @Override
      public void configure (ScopeBinder binder) {
        binder.request (PROFILE, EMAIL, DRIVE);
      }

      /* (non-Javadoc)
       * @see
       * edu.dfci.cccb.mev.googleplus.domain.guice.GoogleDomainModule#configure
       * (edu.dfci.cccb.mev.googleplus.domain.guice.ServicesScopeBindingBuilder) */
      @Override
      public void configure (ServicesScopeBindingBuilder bind) {
        bind.in (SessionScoped.class);
      }
    });

    binder.install (new MevServiceModule () {
      /* (non-Javadoc)
       * @see
       * edu.dfci.cccb.mev.common.services.guice.MevServiceModule#configure
       * (edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder) */
      @Override
      public void configure (ResourceBinder binder) {
        binder.publish (AuthorizationController.class);
      }
    });
  }
}
