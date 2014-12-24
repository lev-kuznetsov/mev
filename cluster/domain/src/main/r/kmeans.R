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

# K means clustering API
#
# 
#
# author: levk
shim ('cluster', callback = function (pam)
  define ('kmeans', function (dist) function (data, distance, k) {
    clustering <- pam (dist (data, method = distance), k, diss = TRUE, cluster.only = TRUE);

    clusters <- list ();
    for (index in 1:k) {
      cluster <- NULL;
      for (entry in names (clustering)) if (clustering[[ entry ]] == index) cluster <- c (cluster, entry);
      clusters[[ index ]] <- cluster;
    }
    clusters;
  }), binder = binder ());