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
 * Core mev module
 * 
 * @author levk
 * @since CRYSTAL
 */
define ('mev', [ 'angular', './log/log', './log/debug', './log/info', './log/warn', './log/error' ], function (ng,
                                                                                                               t,
                                                                                                               d,
                                                                                                               i,
                                                                                                               w,
                                                                                                               e) {
  var mev = ng.module ('mev', []);

  mev.config ([ '$provide', function ($provide) {
    $provide.decorator ('$log', [ function () {
      return {
        log : t,
        debug : d,
        info : i,
        warn : w,
        error : e
      };
    } ]);
  } ]);

  mev.module = function (name, dependencies) {
    attach = function (parent, name, dependencies) {
      parent.requires.push (name);
      var module = ng.module (name, dependencies === undefined ? [] : dependencies);
      module.module = function (name, dependencies) {
        attach (module, name, dependencies);
      };
      return module;
    };

    return attach (mev, name, dependencies);
  };

  mev.bootstrap = function (element) {
    ng.bootstrap (element, [ mev.name ]);
  };

  return mev;
});
