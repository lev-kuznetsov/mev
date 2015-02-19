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
package edu.dfci.cccb.mev.dataset.domain.r;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysisBuilder;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Callback;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Parameter;
import edu.dfci.cccb.mev.dataset.domain.r.annotation.Rserve;

public abstract class AbstractDispatchedRAnalysisBuilder <B extends AbstractDispatchedRAnalysisBuilder<?, ?>, A extends Analysis> extends AbstractAnalysisBuilder<B, A> {

  private @Getter @Setter @Inject @Rserve RDispatcher r;
  private CountDownLatch latch;

  @Override
  @Parameter
  protected Dataset dataset () {
    return super.dataset ();
  }

  public AbstractDispatchedRAnalysisBuilder (String type) {
    super (type);
  }

  protected abstract A result ();

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder#build() */
  @Override
  @SneakyThrows (InterruptedException.class)
  public A build () throws DatasetException {
    synchronized (this) {
      if (latch != null)
        throw new IllegalStateException ();
      latch = new CountDownLatch (1);
    }
    r.schedule (this);
    latch.await ();
    return result ();
  }

  @Callback
  private synchronized void cb () {
    latch.countDown ();
    latch = null;
  }
}
