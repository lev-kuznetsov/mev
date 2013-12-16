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
package edu.dfci.cccb.mev.io.implementation;

import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * @author levk
 * 
 */
public class TemporaryFolderTest {

  private static Set<String> tempContent () {
    return new HashSet<> (asList (new File (getProperty ("java.io.tmpdir")).list ()));
  }

  @Test
  public void test () throws IOException {
    Set<String> before = tempContent ();
    try (TemporaryFolder temp = new TemporaryFolder ()) {
      Set<String> added = tempContent ();
      added.removeAll (before);
      assertEquals (1, added.size ());
      assertEquals (temp.getName (), added.iterator ().next ());
      String test = "hello world";
      File file = new File (temp, "test.f");
      try (Writer writer = new OutputStreamWriter (new FileOutputStream (file))) {
        writer.write (test);
        writer.flush ();
      }
      try (BufferedReader reader = new BufferedReader (new InputStreamReader (new FileInputStream (file)))) {
        assertEquals (test, reader.readLine ());
      }
      // file.delete ();
    }
    assertEquals (before, tempContent ());
  }
}
