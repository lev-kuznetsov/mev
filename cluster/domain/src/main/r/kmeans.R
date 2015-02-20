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
# Defines a 'kmeans' which injects a function accepting a dataframe, the
# distance metric and the number of desired clusters
#
# author: levk
shim ('cluster', callback = function (pam) {
  group <- function (clustering) {
    clusters <- list ();
    for (index in 1:length (clustering)) {
      cluster <- NULL;
      for (entry in names (clustering)) if (clustering[[ entry ]] == index) cluster <- c (cluster, entry);
      clusters[[ index ]] <- cluster;
    }
    clusters;
  };

  define ('kmeans', function (dist) function (data, distance, k, dimension, subset) {
    data <- if (dimension$name == 'column') t (data) else data;
    data <- if (length (subset) < 1) data else data[subset,];
    group (pam (dist (data, method = distance$type), k, diss = TRUE, cluster.only = TRUE))
  });
}, binder = binder ());