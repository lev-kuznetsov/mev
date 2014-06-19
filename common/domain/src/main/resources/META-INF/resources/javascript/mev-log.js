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
 * MeV logging module
 * 
 * @author levk
 * @since CRYSTAL
 */
define ('mev-log', [ 'log4js' ], function (log4js) {
  var logger = log4js.getLogger ("mev");

  function AppliedBrowserConsoleAppender () {}

  AppliedBrowserConsoleAppender.prototype = new log4js.BrowserConsoleAppender ();
  AppliedBrowserConsoleAppender.prototype.append = function (event) {
    if (typeof console != "undefined") {
      var message = this.getLayout ().format (event);
      if (!(message instanceof Array)) message = [ messasge ];

      if (console.debug && log4js.Level.DEBUG.isGreaterOrEqual (event.level)) {
        console.debug.apply (console, message);
      } else if (console.info && log4js.Level.INFO.isGreaterOrEqual (event.level)) {
        console.info.apply (console, message);
      } else if (console.warn && log4js.Level.WARN.isGreaterOrEqual (event.level)) {
        console.warn.apply (console, message);
      } else if (console.error && log4js.Level.ERROR.isGreaterOrEqual (event.level)) {
        console.error.apply (console, message);
      } else if (console.log) console.log.apply (console, message);
    }
  };
  log4js.AppliedBrowserConsoleAppender = AppliedBrowserConsoleAppender;

  var appender = new AppliedBrowserConsoleAppender ();
  appender.setLayout (new log4js.NullLayout ());
  logger.addAppender (appender);

  logger.info ("Initialized log");

  return logger;
});