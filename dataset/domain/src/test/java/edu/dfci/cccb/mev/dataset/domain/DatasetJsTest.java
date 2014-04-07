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

package edu.dfci.cccb.mev.dataset.domain;

import static edu.dfci.cccb.mev.common.test.js.Javascript.js;
import static java.lang.Double.parseDouble;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.script.ScriptEngine;

import org.junit.Before;
import org.junit.Test;

public class DatasetJsTest {

  private ScriptEngine js;

  @Before
  public void setUpJsAndDatasetInJson () throws Exception {
    js = js ().scan ("/META-INF/resources/javascript").engine ();
    js.eval ("var _dataset = new Dataset (JSON.parse ('"
             + "{\"name\":\"mock\",\"dimensions\":[{\"name\":\"row\",\"keys\":[\"r"
             + "1\",\"r2\",\"r3\"]},{\"name\":\"column\",\"keys\":[\"c1\",\"c2\""
             + ",\"c3\",\"c4\"]}],\"values\":[{\"coordinates\":{\"column\":\"c1\""
             + ",\"row\":\"r1\"},\"value\":0.0},{\"coordinates\":{\"column\":\"c"
             + "2\",\"row\":\"r1\"},\"value\":0.1},{\"coordinates\":{\"column\":"
             + "\"c3\",\"row\":\"r1\"},\"value\":0.2},{\"coordinates\":{\"column"
             + "\":\"c4\",\"row\":\"r1\"},\"value\":0.3},{\"coordinates\":{\"col"
             + "umn\":\"c1\",\"row\":\"r2\"},\"value\":-0.1},{\"coordinates\":{\""
             + "column\":\"c2\",\"row\":\"r2\"},\"value\":-0.2},{\"coordinates\""
             + ":{\"column\":\"c3\",\"row\":\"r2\"},\"value\":-0.3},{\"coordinat"
             + "es\":{\"column\":\"c4\",\"row\":\"r2\"},\"value\":-0.4},{\"coord"
             + "inates\":{\"column\":\"c1\",\"row\":\"r3\"},\"value\":0.11},{\"c"
             + "oordinates\":{\"column\":\"c2\",\"row\":\"r3\"},\"value\":-0.22}"
             + ",{\"coordinates\":{\"column\":\"c3\",\"row\":\"r3\"},\"value\":0"
             + ".33},{\"coordinates\":{\"column\":\"c4\",\"row\":\"r3\"},\"value"
             + "\":-0.44}],\"analyses\":[]}'));");
  }

  @Test
  public void datasetName () throws Exception {
    assertThat (js.eval ("_dataset.name ();").toString (), is ("mock"));
  }

  @Test
  public void dimensionName () throws Exception {
    assertThat (js.eval ("_dataset.dimensions ()[0].name ()").toString (), is ("row"));
    assertThat (js.eval ("_dataset.dimensions ()[1].name ()").toString (), is ("column"));
  }

  @Test
  public void dimensionApply () throws Exception {
    js.eval ("var _row1 = _dataset.dimensions ()[0].apply (function (_key) { return _key === 'r1'; },"
             + "function (_value) { return _value; });");
    js.eval ("var _row2 = _dataset.dimensions ()[0].apply (function (_key) { return _key === 'r2'; },"
             + "function (_value) { return _value; });");
    assertThat (parseDouble (js.eval ("_row1[0].value").toString ()), is (.0));
    assertThat (parseDouble (js.eval ("_row1[1].value").toString ()), is (.1));
    assertThat (parseDouble (js.eval ("_row1[2].value").toString ()), is (.2));
    assertThat (parseDouble (js.eval ("_row1[3].value").toString ()), is (.3));
    assertThat (js.eval ("_row1[0].coordinates.column").toString (), is ("c1"));
    assertThat (js.eval ("_row1[1].coordinates.column").toString (), is ("c2"));
    assertThat (js.eval ("_row1[2].coordinates.column").toString (), is ("c3"));
    assertThat (js.eval ("_row1[3].coordinates.column").toString (), is ("c4"));
    assertThat (parseDouble (js.eval ("_row2[0].value").toString ()), is (-.1));
    assertThat (parseDouble (js.eval ("_row2[1].value").toString ()), is (-.2));
    assertThat (parseDouble (js.eval ("_row2[2].value").toString ()), is (-.3));
    assertThat (parseDouble (js.eval ("_row2[3].value").toString ()), is (-.4));
    assertThat (js.eval ("_row2[0].coordinates.column").toString (), is ("c1"));
    assertThat (js.eval ("_row2[1].coordinates.column").toString (), is ("c2"));
    assertThat (js.eval ("_row2[2].coordinates.column").toString (), is ("c3"));
    assertThat (js.eval ("_row2[3].coordinates.column").toString (), is ("c4"));
  }

  @Test
  public void valuesGet () throws Exception {
    assertThat (parseDouble (js.eval ("_dataset.values ().get ({ \"row\": \"r2\", \"column\": \"c2\" });")
                               .toString ()),
                is (-.2));
  }
}
