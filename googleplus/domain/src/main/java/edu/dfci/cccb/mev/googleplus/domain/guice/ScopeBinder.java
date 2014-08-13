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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Binds Google API scopes
 * 
 * @author levk
 * @since CRYSTAL
 */
public interface ScopeBinder {

  /**
   * Typechecked Google API scopes
   */
  public interface Scope {
    String value ();
  }

  /**
   * Some predefined scopes
   */
  @RequiredArgsConstructor
  @Accessors (fluent = true)
  public static enum Scopes implements Scope {
    PROFILE ("https://www.googleapis.com/auth/userinfo.profile"),
    EMAIL ("https://www.googleapis.com/auth/userinfo.email"),
    DRIVE ("https://www.googleapis.com/auth/drive.file");

    private @Getter final String value;
  }

  /**
   * @param scopes to add
   */
  void request (Iterable<Scope> scopes);

  /**
   * @param scopes to add
   */
  void request (Scope... scopes);
}
