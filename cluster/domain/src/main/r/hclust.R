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

# Hierarchical clustering API
#
# Defines a 'hclust' which injects a function accepting a dataframe,
# distance and linkage methods
#
# Binary, canberra, euclidean, manhattan and maximum and average, centroid,
# complete, mcquitty and ward distance and linkage methods respectively are
# supported
#
# Attempts to load the gputools package and if succeeds will use the
# accelerated implementation, if not will fallback on hclust and dist
#
# Result is a recursive list of nodes with left right and dist memebers
#
# author: levk
# since: CRYSTAL
shim ('gputools', 'ctc', callback = function (gpuDistClust, hc2Newick)
  if (missing (gpuDistClust))
    define ('hclust', function (dist) function (data, distance, linkage)
      hc2Newick (hclust (dist (data, method = distance), method = linkage), FALSE))
  else
    define ('hclust', function () function (data, distance, linkage)
      hc2Newick (gpuDistClust (data, dist = distance, clust = linkage), FALSE)), binder = binder ());