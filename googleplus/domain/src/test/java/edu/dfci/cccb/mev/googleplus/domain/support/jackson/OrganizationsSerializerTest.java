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

import com.google.api.services.plus.model.Person.Organizations;

public class OrganizationsSerializerTest extends JsonSerializerTest {

  private Organizations orgs;

  public OrganizationsSerializerTest () {
    super (Organizations.class, OrganizationsSerializer.class);
  }

  @Before
  public void setUpOrgs () {
    orgs = new Organizations ();
    orgs.setDepartment ("mockDept");
    orgs.setDescription ("mockDesc");
    orgs.setEndDate ("mockEnd");
    orgs.setLocation ("mockLoc");
    orgs.setName ("mockName");
    orgs.setStartDate ("mockStart");
    orgs.setTitle ("mockTitle");
    orgs.setType ("mockType");
    orgs.setPrimary (true);
  }

  @Test
  public void orgs () throws Exception {
    assertEquals ("{department:\"mockDept\",description:\"mockDesc\",\"end-date\":\"mockEnd\",location:\"mockLoc\",name:\"mockName\",\"start-date\":\"mockStart\",title:\"mockTitle\",type:\"mockType\",primary:true}",
                  mapper.writeValueAsString (orgs),
                  true);
  }
}
