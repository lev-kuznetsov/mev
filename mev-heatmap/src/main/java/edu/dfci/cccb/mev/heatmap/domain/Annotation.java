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
package edu.dfci.cccb.mev.heatmap.domain;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author levk
 * 
 */
public interface Annotation {

  public static final String INDEX_ATTRIBUTE = "_mev_index";

  public static final String ID_ATTRIBUTE = "_mev_id";

  Result interval (Interval interval);

  public interface Query {}

  public interface Result {
    Iterable<Integer> indices ();

    <T> T get (int index, String attribute);
  }

  public interface Builder {
    Builder quote (char quote);

    Annotation build (InputStream input) throws IOException;
  }
}
