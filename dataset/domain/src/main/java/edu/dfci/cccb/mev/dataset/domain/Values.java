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

import static java.util.Collections.unmodifiableMap;
import static javax.xml.bind.annotation.XmlAccessType.NONE;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Value store
 * 
 * @author levk
 * @since BAYLIE
 */
@XmlRootElement
@XmlAccessorType (NONE)
public interface Values <K, V> {

  /**
   * A single value in the store
   * 
   * @author levk
   * @since CRYSTAL
   */
  @XmlRootElement
  @XmlAccessorType (NONE)
  @Accessors (fluent = true)
  @ToString
  public static class Value <K, V> {
    /**
     * @return coordinate set
     */
    private final @Getter @XmlAttribute Map<String, K> coordinates;

    /**
     * @return value
     */
    private final @Getter @XmlAttribute V value;

    /**
     * @param value
     * @param coordinates
     */
    public Value (V value, Map<String, K> coordinates) {
      this.value = value;
      this.coordinates = unmodifiableMap (new HashMap<String, K> (coordinates));
    }
  }

  /**
   * @param coordinates
   * @return values for the coordinates specified
   * @throws InvalidCoordinateSetException
   */
  Iterable<Value<K, V>> get (Iterable<Map<String, K>> coordinates) throws InvalidCoordinateSetException;
}
