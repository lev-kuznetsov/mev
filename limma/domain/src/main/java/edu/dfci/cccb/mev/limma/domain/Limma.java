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

package edu.dfci.cccb.mev.limma.domain;

import static javax.xml.bind.annotation.XmlAccessType.NONE;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Parameter;
import edu.dfci.cccb.mev.common.domain.jobs.annotation.Result;
import edu.dfci.cccb.mev.common.domain.jobs.r.annotation.R;
import edu.dfci.cccb.mev.dataset.domain.annotation.Analysis;
import edu.dfci.cccb.mev.dataset.domain.r.RAnalysisAdapter;

/**
 * @author levk
 * @since CRYSTAL
 */
@Analysis ("limma")
@XmlRootElement
@XmlAccessorType (NONE)
@R ("function (limma, dataset, experiment, control) limma (dataset, experiment, control)")
public class Limma <K, V> extends RAnalysisAdapter<K, V> {

  private final @Parameter Collection<K> experiment = keys ();
  private final @Parameter Collection<K> control = keys ();

  @XmlRootElement
  @XmlAccessorType (NONE)
  @EqualsAndHashCode
  @ToString
  public static final class Entry {

    private @XmlElement Double logFoldChange;
    private @XmlElement Double averageExpression;
    private @XmlElement Double pValue;
    private @XmlElement Double qValue;
  }

  private @Result Map<K, Entry> limma;

  @Path ("/experiment")
  public Collection<K> experiment () {
    return experiment;
  }

  @XmlElement
  public void experiment (Collection<K> experiment) {
    this.experiment.clear ();
    this.experiment.addAll (experiment);
  }

  @Path ("/control")
  public Collection<K> control () {
    return experiment;
  }

  @XmlElement
  public void control (Collection<K> control) {
    this.control.clear ();
    this.control.addAll (control);
  }

  @GET
  @Path ("/limma")
  public Map<K, Entry> table () {
    return limma;
  }
}
