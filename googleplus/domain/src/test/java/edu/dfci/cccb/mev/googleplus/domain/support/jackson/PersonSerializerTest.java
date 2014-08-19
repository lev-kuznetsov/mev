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

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.google.api.services.plus.model.Person;
import com.google.api.services.plus.model.Person.Emails;
import com.google.api.services.plus.model.Person.Organizations;
import com.google.api.services.plus.model.Person.PlacesLived;
import com.google.api.services.plus.model.Person.Urls;

public class PersonSerializerTest extends JsonSerializerTest {

  private Person person;

  public PersonSerializerTest () {
    super (Person.class, PersonSerializer.class);
  }

  @Before
  public void setUpPerson () {
    person = new Person ();
    person.setAboutMe ("mockAbout");
    person.setBirthday ("mockBirth");
    person.setBraggingRights ("mockBrag");
    person.setCurrentLocation ("mockLoc");
    person.setDisplayName ("mockDisplay");
    person.setDomain ("mockDomain");
    person.setGender ("mockGender");
    person.setId ("mockId");
    person.setLanguage ("mockLang");
    person.setNickname ("mockNickname");
    person.setObjectType ("mockType");
    person.setOccupation ("mockOccupation");
    person.setRelationshipStatus ("mockRel");
    person.setSkills ("mockSkills");
    person.setTagline ("mockTagline");
    person.setUrl ("mock://mock");
    person.setCircledByCount (11);
    person.setPlusOneCount (22);
    person.setVerified (true);
    person.setIsPlusUser (false);
    person.setCover (null);
    person.setEmails (new ArrayList<Emails> ());
    person.setImage (null);
    person.setOrganizations (new ArrayList<Organizations> ());
    person.setPlacesLived (new ArrayList<PlacesLived> ());
    person.setUrls (new ArrayList<Urls> ());
  }

  @Test
  public void person () throws Exception {
    assertEquals ("{about:\"mockAbout\",birthday:\"mockBirth\",\"bragging-rights\":\"mockBrag\",location:\"mockLoc\"," +
                  "display:\"mockDisplay\",domain:\"mockDomain\",gender:\"mockGender\",id:\"mockId\"," +
                  "language:\"mockLang\",nickname:\"mockNickname\",type:\"mockType\",occupation:\"mockOccupation\"," +
                  "relationship:\"mockRel\",skills:\"mockSkills\",tagline:\"mockTagline\",url:\"mock://mock\"," +
                  "circled-by:11,plus-one-count:22,verified:true,is-plus-user:false,cover:null,emails:[]," +
                  "image:null,organizations:[],places-lived:[],urls:[]}",
                  mapper.writeValueAsString (person), true);
  }
}
