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
shim ('gputools', 'ctc', callback = function (gpuDistClust = NULL, hc2Newick) {
  node <- function (tree)
    if (typeof (tree) == 'character') list (type = 'leaf', name = tree)
    else list (left = node (tree$left), right = node (tree$right), distance = tree$dist, type = 'branch');

  define ('hclust', function (dist)
    function (data, distance, linkage, dimension, subset) {
      data <- if (dimension$name == 'column') t (data) else data;
      data <- if (length (subset) < 1) data else data[subset,];
      node (hc2Newick (if (is.null (gpuDistClust)) hclust (dist (data, met = distance$type), met = linkage$type)
                       else gpuDistClust (data, dist = distance$type, clust = linkage$type), FALSE))
  }, scope = singleton)
}, binder = binder ());
