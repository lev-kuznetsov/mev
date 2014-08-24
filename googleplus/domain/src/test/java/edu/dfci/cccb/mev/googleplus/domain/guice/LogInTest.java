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

package edu.dfci.cccb.mev.googleplus.domain.guice;

import org.junit.Ignore;
import org.junit.Test;

import com.google.api.services.drive.Drive;
import com.google.inject.Guice;

import edu.dfci.cccb.mev.googleplus.domain.guice.ScopeBinder.Scopes;

public class LogInTest {

  @Ignore
  @Test
  public void lol () throws Exception {
    System.out.println (Guice.createInjector (new HeadlessGoogleLoginModule () {
      @Override
      public void configure (ScopeBinder binder) {
        binder.request (Scopes.values ());
      }
    }).getInstance (Drive.class).files ().list ().execute ());
  }
}
