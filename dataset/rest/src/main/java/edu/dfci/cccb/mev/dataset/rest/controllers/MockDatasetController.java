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
package edu.dfci.cccb.mev.dataset.rest.controllers;

import static java.util.UUID.randomUUID;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.rest.json.Data;
import edu.dfci.cccb.mev.dataset.rest.json.Value;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/dataset/mock")
@Scope ("session")
public class MockDatasetController {

  private final Random rnd = new Random ();
  private List<String> rows;
  private List<String> columns;
  private Collection<Value> values;

  {
    rows = new ArrayList<> ();
    columns = new ArrayList<> ();
    values = new ArrayList<> ();
    for (int r = 100 + rnd.nextInt (100); --r >= 0; rows.add ("ROW-" + randomUUID ()));
    for (int c = 100 + rnd.nextInt (100); --c >= 0; columns.add ("COLUMN-" + randomUUID ()));
    for (int r = rows.size (); --r >= 0;)
      for (int c = columns.size (); --c >= 0; values.add (new Value (rnd.nextDouble () * 5 - 2.5,
                                                                     rows.get (r),
                                                                     columns.get (c))));
  }

  @RequestMapping (method = GET)
  public Data mockData () {
    return new Data (rows, columns, values);
  }
}
