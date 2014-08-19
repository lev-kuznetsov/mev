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

package edu.dfci.cccb.mev.googleplus.domain.support.jackson;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.api.services.drive.model.User;
import com.google.api.services.drive.model.User.Picture;

public class UserSerializerTest extends JsonSerializerTest {

  private User user;

  public UserSerializerTest () {
    super (User.class, UserSerializer.class);
  }

  @Before
  public void setUpUser () {
    user = new User ();
    user.setDisplayName ("mockName");
    user.setEmailAddress ("mock@email.com");
    user.setPicture (new Picture ().setUrl ("mock://mock"));
  }

  @Test
  public void user () throws Exception {
    assertEquals ("{name:\"mockName\",email:\"mock@email.com\",picture:\"mock://mock\"}",
                  mapper.writeValueAsString (user),
                  true);
  }
}
