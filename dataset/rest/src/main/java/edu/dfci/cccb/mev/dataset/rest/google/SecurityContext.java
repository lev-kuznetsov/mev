/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.rest.google;


/**
 * @author levk
 * 
 */
public class SecurityContext {
  private static final ThreadLocal<User> currentUser = new ThreadLocal<User> ();

  public static User getCurrentUser () {
    User user = currentUser.get ();
    if (user == null) {
      throw new IllegalStateException ("No user is currently signed in");
    }
    return user;
  }

  public static void setCurrentUser (User user) {
    currentUser.set (user);
  }

  public static boolean userSignedIn () {
    return currentUser.get () != null;
  }

  public static void remove () {
    currentUser.remove ();
  }
}
