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
 * log4javascript logger
 * 
 * @author levk
 * @since CRYSTAL
 */
define ([ 'log4javascript', 'underscore' ], function (l4js, _) {
  function AppliedBrowserConsoleAppender () {}

  AppliedBrowserConsoleAppender.prototype = new l4js.BrowserConsoleAppender ();
  AppliedBrowserConsoleAppender.prototype.append = function (event) {
    if (typeof console != "undefined") {
      var message = this.getLayout ().format (event);
      if (!(message instanceof Array)) message = [ messasge ];

      try {
        if (l4js.Level.TRACE.isGreaterOrEqual (event.level)) console.log.apply (console, message);
        else if (l4js.Level.DEBUG.isGreaterOrEqual (event.level)) console.debug.apply (console, message);
        else if (l4js.Level.INFO.isGreaterOrEqual (event.level)) console.info.apply (console, message);
        else if (l4js.Level.WARN.isGreaterOrEqual (event.level)) console.warn.apply (console, message);
        else if (l4js.Level.ERROR.isGreaterOrEqual (event.level)) console.error.apply (console, message);
      } catch (error) {
        alert ("Unable to log");
      }
    }
  };
  l4js.AppliedBrowserConsoleAppender = AppliedBrowserConsoleAppender;
  var appender = new AppliedBrowserConsoleAppender ();
  appender.setLayout (new l4js.NullLayout ());
  appender.setThreshold (l4js.Level.ALL);

  var logger = l4js.getLogger ("mev");
  logger.addAppender (appender);
  logger.setLevel (l4js.Level.ALL);

  _.bindAll.apply (_, [ logger ].concat (_.functions (logger)));

  return logger;
});
