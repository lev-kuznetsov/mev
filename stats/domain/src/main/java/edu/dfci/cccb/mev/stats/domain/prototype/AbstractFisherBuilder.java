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

import static edu.dfci.cccb.mev.stats.domain.contract.Hypothesis.TWO_SIDED;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysisBuilder;
import edu.dfci.cccb.mev.stats.domain.contract.Fisher;
import edu.dfci.cccb.mev.stats.domain.contract.FisherBuilder;
import edu.dfci.cccb.mev.stats.domain.contract.Hypothesis;

/**
 * @author levk
 * 
 */
@Accessors (fluent = true, chain = true)
public abstract class AbstractFisherBuilder extends AbstractAnalysisBuilder<FisherBuilder, Fisher> implements FisherBuilder {

  protected AbstractFisherBuilder () {
    super ("Fisher test");
  }

  private @Getter (PROTECTED) @Setter int m;
  private @Getter (PROTECTED) @Setter int n;
  private @Getter (PROTECTED) @Setter int s;
  private @Getter (PROTECTED) @Setter int t;
  private @Getter (PROTECTED) @Setter Hypothesis hypothesis = TWO_SIDED;
  private @Getter (PROTECTED) @Setter boolean simulate = false;
}
