# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software Foundation,
# Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

# Basic logging facilities 
#
# author: levk
# since CRYSTAL
shim ('logging', callback = function (basicConfig, loglevels, logdebug, loginfo, logwarn, logerror) {
  basicConfig (loglevels[[ 'DEBUG' ]]);

  define ('debug', function (module = '')
                     function (message) logdebug (message, logger = module));
  define ('info', function (module = '')
                    function (message) loginfo (message, logger = module));
  define ('warn', function (module = '')
                    function (message) logwarn (message, logger = module));
  define ('error', function (module = '')
                     function (message) logerror (message, logger = module));
}, binder = binder ());
