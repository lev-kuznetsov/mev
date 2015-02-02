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

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
@Analysis ("topGo")
@XmlRootElement
@XmlAccessorType (NONE)
@R ("function (topGo, species) 1")
public class TopGo <K, V> extends RAnalysisAdapter<K, V> {

  private @Parameter Species species;

  @XmlRootElement
  @XmlAccessorType (NONE)
  @ToString
  @EqualsAndHashCode
  public static final class Entry {

    private @XmlElement String term;
    private @XmlElement String annotated;
    private @XmlElement String significant;
    private @XmlElement String expected;
    private @XmlElement Double pValue;
  }

  private @Result Map<K, Entry> topGo;

  @PUT
  @Path ("/species")
  @XmlElement (name = "species")
  public void set (Species species) {
    this.species = species;
  }

  @GET
  @Path ("/topGo")
  public Map<K, Entry> table () {
    return topGo;
  }
}
