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
package edu.dfci.cccb.mev.hcl.domain.prototype;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalysis;
import edu.dfci.cccb.mev.hcl.domain.contract.Hcl;
import edu.dfci.cccb.mev.hcl.domain.contract.HclResult;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;

/**
 * @author levk
 * 
 */
@ToString (exclude = "dataset")
@EqualsAndHashCode (callSuper = true)
@Accessors (fluent = true, chain = true)
@JsonIgnoreProperties({"timestamp", "dimension"})
public abstract class AbstractHcl extends AbstractAnalysis<AbstractHcl> implements Hcl {
  public final static String ANALYSIS_TYPE = "Hierarchical Clustering";
  
  @NoArgsConstructor 
  public static class CHclResult implements HclResult{
	  private @JsonProperty @Getter @Setter Node column;
	  private @JsonProperty @Getter @Setter Node row;	
  }
  
  private @JsonProperty @Getter @Setter CHclResult result;
  private @Setter List<Dimension> dimensions;
  private @Getter @Setter Dataset dataset;
  public AbstractHcl(){
	  type(ANALYSIS_TYPE);
  }
  
  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.hcl.domain.contract.Hcl#dimension() */
  @Override
  public List<Dimension.Type> dimension () {
	List<Type> types = new ArrayList<Dimension.Type>();
    for(Dimension dim : dimensions)
    	types.add(dim.type());
    return types;    
  }
}
