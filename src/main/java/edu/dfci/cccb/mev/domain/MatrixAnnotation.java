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
package edu.dfci.cccb.mev.domain;

import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.ExtensionMethod;

/**
 * A value type for a single matrix annotation
 * 
 * @author levk
 * 
 */
@ExtensionMethod (Collections.class)
@Accessors (fluent = true, chain = false)
@EqualsAndHashCode
public class MatrixAnnotation <T> {

  private @Getter @JsonView final String type;
  private @Getter @JsonView final T value;
  private @Getter @JsonView final Meta meta;
  private @Getter @JsonView final int index;
  private @Getter @JsonView final Collection<? extends T> range;

  @RequiredArgsConstructor
  public static enum Meta {
    QUANTITATIVE ("quantitative"),
    CATEGORICAL ("categorical");

    private final @Getter String meta;

    @Override
    public String toString () {
      return meta;
    }
  }

  public MatrixAnnotation (String type, T value, Meta meta, int index, Collection<? extends T> range) {
    this.type = type;
    this.value = value;
    this.meta = meta;
    this.index = index;
    this.range = range.unmodifiableCollection ();
  }
}
