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
 * Main MeV module
 * 
 * @author levk
 * @since CRYSTAL
 */
define ('mev', [ 'angular', 'log', 'debug', 'info', 'warn', 'error' ], function (ng, log, debug, info, warn, error) {
  var mev = ng.module ('mev', []);

  mev.value ('services-root-uri', '/services');

  mev.config ([ '$provide', function ($provide) {
    $provide.decorator ('$log', [ function () {
      return {
        log : log,
        debug : debug,
        info : info,
        warn : warn,
        error : error
      };
    } ]);
  } ]);

//  mev.$component = function (name, inject, component, requires, binder) {
//    component.$name = name;
//    component.$inject = inject ? inject : [];
//    component.$binder = binder ? binder : function (module) { return module.factory };
//    component.$requires = requires ? requires : [];
//    return component;
//  }

  mev.$module = function (name) {
    mev.requires.push (name);
    
    var components = arguments.splice (1);
    var module = ng.module (name, components.map (function (component) { return component.$requires }));

    module.$component = function (name, inject, component, requires, binder) {
      component.$name = name;
      component.$inject = inject ? inject : [];
      component.$binder = binder ? binder : function () { return module.factory };
      component.$requires = requires ? requires : [];
      return component;
    };

    components.forEach (function (component) {
      component.$binder () (component.$name, component);
    });

    return module;
  };

  return mev;
});
