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
 * MeV dataset services
 * 
 * @author levk
 * @since CRYSTAL
 */
define ('dataset.service', [ 'mev', 'dataset', 'angular', 'angularResource' ], function (mev, dataset, angular) {

  mev.dataset = {
    $resource : {},

    get : function (name, success, error) {
      return mev._.extend ($resource ('/dataset/:name').get ({
        name : name,
        format : 'json'
      }, success, error), dataset);
    },

    query : function (success, error) {
      return $resource ('/dataset').query ({
        format : 'json'
      }, success, error);
    }
  }

  angular.module ('mev', [ 'ngResource' ]).run ([ '$resource', function ($resource) {
    mev.dataset.$resource = $resource;
  } ]);

  return mev.dataset;
});