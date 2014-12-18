/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.dataset.domain.r;

import javax.inject.Inject;
import javax.inject.Provider;

import edu.dfci.cccb.mev.common.domain.jobs.r.R;
import edu.dfci.cccb.mev.dataset.domain.AnalysisInvocationException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AnalysisAdapter;

/**
 * Common adapter for all R based analyses
 * 
 * @author levk
 * @since CRYSTAL
 */
public abstract class RAnalysisAdapter extends AnalysisAdapter {

  private @Inject Provider<R> provider;

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.Analysis#run() */
  @Override
  public final void run () throws AnalysisInvocationException {
    try (R injector = provider.get ()) {
      injector.inject (this);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new AnalysisInvocationException (e);
    }
  }
}
