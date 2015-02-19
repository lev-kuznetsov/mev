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
package edu.dfci.cccb.mev.anova.domain.impl;

import java.util.List;

import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.anova.domain.contract.Anova2;
import edu.dfci.cccb.mev.anova.domain.prototype.AbstractAnova2;
import edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;

/**
 * @author levk
 * 
 */
@R ("function (anova, dataset, group, pValue) anova (dataset, group, pValue)")
@Accessors (fluent = true)
public class DispatchedAnovaBuilder extends AbstractDispatchedRAnalysisBuilder<DispatchedAnovaBuilder, Anova2> {

  private @Parameter String[] group;
  private @Parameter double pValue;

  private @Result List<SimpleEntry2> result;

  public DispatchedAnovaBuilder () {
    super ("Anova Analysis");
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.r.AbstractDispatchedRAnalysisBuilder#
   * result() */
  @Override
  protected Anova2 result () {
    return new AbstractAnova2 () {

      @Override
      public Iterable<? extends Entry2> entries () {
        return result;
      }
    };
  }
}
