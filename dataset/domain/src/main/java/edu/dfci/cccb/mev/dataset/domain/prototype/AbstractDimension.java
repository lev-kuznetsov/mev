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
package edu.dfci.cccb.mev.dataset.domain.prototype;

import static lombok.AccessLevel.PROTECTED;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import edu.dfci.cccb.mev.dataset.domain.contract.Annotation;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.Selections;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListSelections;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode
@Accessors (fluent = true)
@RequiredArgsConstructor (access = PROTECTED)
public abstract class AbstractDimension implements Dimension {

  private @Getter final Type type;
  private @Getter @Setter (PROTECTED) Selections selections;
  private @Getter @Setter Annotation annotation;

  /* (non-Javadoc)
   * @see java.lang.Object#toString() */
  @Override
  public String toString () {
    return getClass ().getSimpleName ()
           + "(type=" + type () + ", "
           + "selections=" + selections + ", "
           + "keys=" + (keys ().size () > 10 ? "<" + keys ().size () + " keys>" : keys ()) + ")";
  }
  
  @Override
  public Dimension subset (List<String> keys) {
	Selections subsetSelections = new ArrayListSelections();
	if(keys == null)
		keys = this.keys();
	for(Selection selection : this.selections.getAll()){				
		for(String key : keys)
			if(selection.keys().contains(key)){
				subsetSelections.put(selection);
				break;
			}	
	}
	return new SimpleDimension (type, keys, subsetSelections, annotation);
  }
}
