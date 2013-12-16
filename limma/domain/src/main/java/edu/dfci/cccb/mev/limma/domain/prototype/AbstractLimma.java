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
package edu.dfci.cccb.mev.limma.domain.prototype;

import javax.inject.Inject;
import javax.script.ScriptEngine;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.ComposerFactory;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysisBuilder;
import edu.dfci.cccb.mev.limma.domain.contract.Limma;
import edu.dfci.cccb.mev.limma.domain.contract.LimmaResult;

/**
 * @author levk
 * 
 */
@ToString
@Accessors (fluent = true, chain = true)
public abstract class AbstractLimma extends AbstractAnalysisBuilder<Limma, LimmaResult> implements Limma {

  private @Getter @Setter Selection control;
  private @Getter @Setter Selection experiment;
  private @Getter @Setter double alpha;
  private @Getter @Setter (onMethod = @_ (@Inject)) ScriptEngine r;
  private @Getter @Setter (onMethod = @_ (@Inject)) ComposerFactory composerFactory;

  /**
   */
  public AbstractLimma () {
    super ("LIMMA");
  }
}
