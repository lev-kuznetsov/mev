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

# Tools for inverting groupings in relation to dataframes
#
# author: levk

# Constructs a correlation list formed in the order of the specified
# margin of the specified dataframe correlating to its group
define ('pivot', function () function (df, margin, groups)
  lapply (setNames (dimnames (df)[[ margin ]], dimnames (df)[[ margin ]]), function (name)
    unlist (lapply (if (is.null (names (groups))) 1:length (groups) else names (groups), function (group)
      if (name %in% groups[[ group ]]) group), use.names = FALSE)));

# Constructs a vector ensuring at most (and optionally at least) single
# group membership
define ('relate', function (pivot, relate.allow.unrelated = FALSE)
  function (df, margin, groups, allow.unrelated = relate.allow.unrelated)
    sapply (pivot (df, margin, groups), function (x)
      if (length (x) > 1) stop (paste ("Multiple relationship element"))
      else if (length (x) < 1 && allow.unrelated) NA
      else if (length (x) < 1) stop ("Unrelated element")
      else x));