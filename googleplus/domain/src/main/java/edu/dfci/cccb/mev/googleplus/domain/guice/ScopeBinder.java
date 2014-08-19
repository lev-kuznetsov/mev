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

import static com.google.api.services.drive.DriveScopes.DRIVE_APPDATA;
import static com.google.api.services.drive.DriveScopes.DRIVE_APPS_READONLY;
import static com.google.api.services.drive.DriveScopes.DRIVE_FILE;
import static com.google.api.services.drive.DriveScopes.DRIVE_METADATA_READONLY;
import static com.google.api.services.drive.DriveScopes.DRIVE_SCRIPTS;
import static com.google.api.services.plus.PlusScopes.PLUS_LOGIN;
import static com.google.api.services.plus.PlusScopes.PLUS_ME;
import static com.google.api.services.plus.PlusScopes.USERINFO_EMAIL;
import static com.google.api.services.plus.PlusScopes.USERINFO_PROFILE;
import static com.google.api.services.plusDomains.PlusDomainsScopes.PLUS_CIRCLES_READ;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.google.api.services.drive.DriveScopes;

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
    // Plus
    PROFILE (USERINFO_PROFILE),
    EMAIL (USERINFO_EMAIL),
    ME (PLUS_ME),
    LOGIN (PLUS_LOGIN),

    // Drive
    DRIVE (DriveScopes.DRIVE),
    CONFIGURATION (DRIVE_APPDATA),
    SCRIPTS (DRIVE_SCRIPTS),
    APPS (DRIVE_APPS_READONLY),
    METADATA (DRIVE_METADATA_READONLY),
    FILE (DRIVE_FILE),

    // Domains
    CIRCLES (PLUS_CIRCLES_READ);

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
