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
 * MeV thin front end
 * 
 * @author levk
 * @since CRYSTAL
 */
define ('mev-web', [ 'mev-services', 'angular-foundation' ], function (module) {
  module.requires.push ('mm.foundation');

  module.controller ("navigation-controller", function ($scope) {
    $scope.tabs = [ {
      title : "<datasets>",
      content : "<load dataset partial>"
    }, {
      title : "<current dataset>",
      content : "<heatmap partial>"
    } ];
  });
});