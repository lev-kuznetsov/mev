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
define ('mev', [ 'underscore', 'log4js' ], function (underscore) {

  /**
   * Deep check for equality
   */
  Object.prototype.equals = function (o) {
    var deepEquals = function (x, y) {
      if (x === y) return true;
      if (!(x instanceof Object) || !(y instanceof Object)) return false;
      if (x.constructor !== y.constructor) return false;
      for ( var p in x) {
        if (!x.hasOwnProperty (p)) continue;
        if (!y.hasOwnProperty (p)) return false;
        if (x[p] === y[p]) continue;
        if (typeof (x[p]) !== "object") return false;
        if (!Object.deepEquals (x[p], y[p])) return false;
      }

      for ( var p in y)
        if (y.hasOwnProperty (p) && !x.hasOwnProperty (p)) return false;
      return true;
    };

    return deepEquals (this, o);
  };

  return {
    _ : underscore,
    log : (function () {
      var result = log4javascript.getLogger ("mev");
      var appender = new log4javascript.BrowserConsoleAppender ();
      var layout = new log4javascript.SimpleLayout ();
      appender.setLayout (layout);
      result.addAppender (appender);
      return result;
    }) ()
  };
});