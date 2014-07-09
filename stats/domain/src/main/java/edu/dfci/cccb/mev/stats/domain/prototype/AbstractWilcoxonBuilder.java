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
package edu.dfci.cccb.mev.stats.domain.prototype;

import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysisBuilder;
import edu.dfci.cccb.mev.stats.domain.contract.Hypothesis;
import edu.dfci.cccb.mev.stats.domain.contract.Wilcoxon;
import edu.dfci.cccb.mev.stats.domain.contract.WilcoxonBuilder;

/**
 * @author levk
 * 
 */
@Accessors (fluent = true, chain = true)
public abstract class AbstractWilcoxonBuilder extends AbstractAnalysisBuilder<WilcoxonBuilder, Wilcoxon> implements WilcoxonBuilder {

  /**
   * @param type
   */
  public AbstractWilcoxonBuilder () {
    super ("Wilcoxon test");
  }

  private @Getter (PROTECTED) @Setter String first;
  private @Getter (PROTECTED) @Setter String second;
  private @Getter (PROTECTED) @Setter Hypothesis hypothesis = Hypothesis.TWO_SIDED;
  private @Getter (PROTECTED) @Setter boolean pair = false;
  private @Getter (PROTECTED) @Setter boolean confidentInterval = false;
}
