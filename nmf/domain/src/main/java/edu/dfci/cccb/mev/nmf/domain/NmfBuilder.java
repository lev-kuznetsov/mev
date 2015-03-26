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

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.RDispatcher;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Result;

/**
 * @author levk
 * 
 */
@R ("function (dataset, rank, method, seed, nrun, nmf) nmf (dataset, rank, method, nrun)")
@Accessors (fluent = true, chain = true)
public class NmfBuilder extends AbstractAnalysisBuilder<NmfBuilder, Nmf> {

  public NmfBuilder () {
    super ("nmf");
  }

  private @Inject RDispatcher r;

  private @Getter @Setter @Parameter Dataset dataset;
  private @Getter @Setter @Parameter int rank = 3;
  private @Getter @Setter @Parameter String method = "brunet";
  private @Getter @Setter @Parameter int nrun = 10;

  private @Result Nmf result;

  private final CountDownLatch semaphore = new CountDownLatch (1);

  @Callback
  private void done () {
    semaphore.countDown ();
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder#build() */
  @Override
  public Nmf build () throws DatasetException {
    r.schedule (this);
    try {
      semaphore.await ();
    } catch (InterruptedException e) {
      throw new DatasetException (e);
    }
    return result;
  }
}
