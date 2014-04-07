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

/**
 * Values
 * 
 * @author levk
 * @since CRYSTAL
 */
function Values (_dataset) {
  this._dataset = _dataset;

  /**
   * Gets a single value for given coordinates
   */
  this.get = function (_coordinates) {
    for (_value in this._dataset.values)
      if (Object.deepEquals (_coordinates, this._dataset.values[_value].coordinates))
        return this._dataset.values[_value].value;
    throw "Bad coordinate set " + _coordinates;
  }
}