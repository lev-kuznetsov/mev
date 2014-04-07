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
 * Dataset
 * 
 * @author levk
 * @since CRYSTAL
 */
function Dataset (_dataset) {
  this._dataset = _dataset;

  /**
   * Name of the dataset
   */
  this.name = function () {
    return this._dataset.name;
  };

  /**
   * Array of dimension wrappers
   */
  this.dimensions = function () {
    var _dimensions = [];
    for (_dimension in this._dataset.dimensions)
      _dimensions.push (new Dimension (this._dataset, _dimension));
    return _dimensions;
  };

  /**
   * Analyses
   */
  this.analyses = function () {
    return this._dataset.analyses;
  };

  /**
   * Values wrapper
   */
  this.values = function () {
    return new Values (this._dataset);
  };
}
