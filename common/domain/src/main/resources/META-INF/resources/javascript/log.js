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
 * Angular log4javascript logging module
 * 
 * @author levk
 * @since CRYSTAL
 */
define ('log', [ 'angular', 'logger', 'mev' ], function (ng, logger, mev) {
  mev.requires.push ('log');

  return ng.module ('log', []).config ([ '$provide', function ($provide) {
    $provide.decorator ('$log', [ function () {
      return {
        log : function () {
          logger.trace.apply (logger.trace, arguments);
        },
        info : function () {
          logger.info.apply (logger.info, arguments);
        },
        warn : function () {
          logger.warn.apply (logger.warn, arguments);
        },
        error : function () {
          logger.error.apply (logger.error, arguments);
        },
        debug : function () {
          logger.debug.apply (logger.debug, arguments);
        }
      };
    } ]);
  } ]);
});