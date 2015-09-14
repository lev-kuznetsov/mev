/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.nmf.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;

/**
 * @author levk
 * 
 */
// @R
// ("function (dataset, rank, method, nrun, nmf) nmf (dataset, rank, method, nrun)")
// @R
// ("function (dataset, rank, method, nrun) nmf (dataset, rank = rank, method = method, nrun = nrun)")
@R ("function (dataset) {"
    + "hc2n <- function (hc, flat = FALSE) {\n"
    + "  dist <- 0;\n"
    + "  if (is.null (hc$labels)) labels <- seq(along = hc$order) else labels <- hc$labels;\n"
    + "  putparenthesis <- function (i) {\n"
    + "    j <- hc$merge[i, 1];\n"
    + "    k <- hc$merge[i, 2];\n"
    + "    if (j < 0) {\n"
    + "      left <- labels[-j];\n"
    + "      if (k > 0) dist <- hc$height[i] - hc$height[k] else dist <- hc$height[i];\n"
    + "    } else left <- putparenthesis(j);\n"
    + "    if (k < 0) {\n"
    + "      right <- labels[-k];\n"
    + "      if (j > 0) dist <- hc$height[i] - hc$height[j] else dist <- hc$height[i];\n"
    + "    } else right <- putparenthesis(k);\n"
    + "    if (flat) return (paste (\"(\", left, \":\", dist/2, \",\", right, \":\", dist/2, \")\", sep = \"\"))\n"
    + "    else return (list(left = left, right = right, dist = dist));\n"
    + "  }\n"
    + "  n <- putparenthesis (nrow (hc$merge));\n"
    + "  if (flat) n <- paste(n, \";\", sep = \"\");\n"
    + "  return (n);\n"
    + "};\n"
    + "l <- function (n)"
    + "  if (typeof (n) == 'character') list (name = n) "
    + "  else list (distance = n$dist, left = l (n$left), right = l (n$right));"
    + "m <- NMF::nmf (dataset, rank = 3);"
    + "w <- NMF::basis (m);"
    + "h <- NMF::coef (m);"
    + "colnames (w) <- c (1:dim (w)[ 2 ]);"
    + "rownames (h) <- c (1:dim (h)[ 1 ]);"
    + "list (w = as.data.frame (w), h = list (matrix = as.data.frame (h), root = l (hc2n (stats::hclust (stats::dist (t (h)))))));"
    +
    "}")
@Accessors (fluent = true, chain = true)
public class NmfBuilder extends AbstractDispatchedRAnalysisBuilder<NmfBuilder, Nmf> {

  public NmfBuilder () {
    super ("Non-Negative Matrix Factorization");
  }

  private @Getter @Setter @Parameter int rank = 3;
  private @Getter @Setter @Parameter String method = "brunet";
  private @Getter @Setter @Parameter int nrun = 10;
  private @Getter @Setter @Parameter String seed = "nndsvd";

  private @Getter @Result Nmf result;

  @Callback
  private void setName () {
    result.name (name ());
  }
}
