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
 * Dimension
 * 
 * @author levk
 * @since CRYSTAL
 */
function Dimension (_dataset, _dimension) {
  this._dataset = _dataset;
  this._dimension = _dimension;

  /**
   * Name of the dimension (for example "row")
   */
  this.name = function () {
    return this._dataset.dimensions[_dimension].name;
  };

  /**
   * Keys of the dimension
   */
  this.keys = function () {
    return this._dataset.dimensions[_dimension].keys;
  };

  /**
   * @param _filter
   *          to return true/false for each key in the dimension
   * @param _function
   *          to be called for each filtered value, result of the function, if
   *          not null will be collected and returned in a list
   */
  this.apply = function (_filter, _function) {
    var _results = [];
    for (_key in this.keys ())
      if (_filter (this.keys ()[_key])) for (_value in this._dataset.values)
        if (this._dataset.values[_value].coordinates[this.name ()] === this.keys ()[_key]) {
          _result = _function (this._dataset.values[_value]);
          if (_result != null) _results.push (_result);
        }
    return _results;
  }
}
