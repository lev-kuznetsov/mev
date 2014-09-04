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
 * The dataset repository injectable factory
 * 
 * @author levk
 * @since CRYSTAL
 */
define ('ng-workspace', [ 'ng-dataset', 'ng-dataset-template-uri', 'angular-resource' ], function (dataset) {

  dataset.requires.push ('ngResource');

  return dataset.factory ('workspace', [ '$resource', 'dataset-template-uri', function ($resource, uri) {
    var source = $resource (uri, {
      dataset : '@dataset',
      headers : {
        'Accept' : 'application/json'
      },
      actions : {
        download : {
          method : 'get',
          headers : {
            'Accept' : 'text/tab-separated-values'
          }
        },
        upload : {
          method : 'put',
          headers : {
            'Content-Type' : 'text/tab-separated-values'
          }
        }
      }
    });

    return {
      list : function (success, error) {
        return source.query (success, error);
      },

      get : function (name, success, error) {
        return source.get ({
          dataset : name
        }, success, error);
      },

      download : function (name, success, error) {
        source.download ({
          dataset : name
        }, success, error);
      },

      upload : function (name, data, success, error) {
        source.upload ({
          dataset : name
        }, data, success, error);
      }
    };
  } ]);
});