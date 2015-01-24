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
define ('dataset', [ 'angular', 'mev', 'angular-resource' ], function (ng, mev) {
  var dataset = ng.module ('dataset', [ 'ngResource' ]);

  mev.requires.push ('dataset');

  dataset.factory ('dataset-template-uri', [ 'services-root-uri', function (services) {
    return services + "/dataset/:dataset";
  } ]);

  dataset.factory ('analysis-template-uri', [ 'dataset-template-uri', function (dataset) {
    return dataset + "/analysis/:analysis";
  } ]);

  dataset.factory ('workspace', [ '$resource', 'dataset-template-uri', function ($resource, uri) {
    var source = $resource (uri, {
      dataset : '@dataset'
    }, {
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
      },
      list : {
        method : 'get',
        isArray : true,
        headers : {
          'Accept' : 'application/json'
        }
      }
    });

    return {
      list : function (success, error) {
        return source.list (success, error);
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
///----------------------
define ('module', [ 'angular', 'mev' ], function (ng, mev) {
  return function (name) {
    var components = arguments.splice (1);

    mev.requires.push (name);
    var module = ng.module (name, components.map (function (component) { return component.$requires }));

    components.forEach (function (component) {
      module[component.$method](component.$name, component);
    });

    return module;
  };
});

define ('dataset', ['module', 'workspace' ], function (module, ws) {
  module ('dataset', ws);
});

define ('workspace', [ 'angular-resource' ], function () {
  var workspace = function ($resource, uri) {};

  workspace.$inject=["$resource", "dataset-tempalte-uri"];
  workspace.$reqiures = [ 'ngResource' ];
  workspace.$binder = function (module) { return module.factory }
  workspace.$name = 'workspace';

  return workspace;
});


///-----
define ('dataset', [ 'mev', 'workspace' ], function (mev, ws) {
  mev.$module ('dataset', ws);
});

define ('workspace', [ 'dataset', 'angular-resource' ], function (dataset) {
  return dataset.$component ('workspace', [ '$resource', 'dataset-template-uri' ], [ 'ngResource' ], function ($resource, uri) {
//    adsf;klj afl
//    asdfl;a
//    ;asdfj
//    a;sdfj;asdf
  });
});
