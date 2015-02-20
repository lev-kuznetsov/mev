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

# Alternative version of 'apply' applied with submatrices of original
# dataframe preserving row and column names
#
# author: levk
define ('apply', function () function (df, margin, fun)
  lapply (1:length (dimnames (df)[[ margin ]]), function (x) fun (switch (margin, `1` = df[x, ], `2` = df[x]))));
