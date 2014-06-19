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
 * Dataset operations
 * 
 * @author levk
 * @since CRYSTAL
 */
define ('dataset', [ 'mev-log' ], function (log) {

  return {
    dimension : function (name) {
      for (dimension in this.dimensions)
        if (this.dimensions[dimension].name === name) return this.dimensions[dimension];
      throw new Error ("No dimension " + name + " found for dataset " + this.name);
    },

    analysis : function (name) {
      for (analysis in this.analyses)
        if (this.analyses[analysis].name === name) return this.analyses[analysis];
      throw new Error ("No analysis " + name + " found for dataset " + this.name);
    }
  };
});
