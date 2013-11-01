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

public interface Heatmap {

  public static final String HEATMAP_NAME_PATTERN_EXPRESSION = "[^/]+";

  String name ();

  void rename (String name);

  Data data (Area area) throws InvalidDataRequestException;

  Annotation annotation (Axis axis);

  public interface Builder {

    public interface Content {
      InputStream data () throws IOException;

      String name ();

      String type ();

      long size ();
    }

    Heatmap build (Content content) throws IOException;
  }
}
