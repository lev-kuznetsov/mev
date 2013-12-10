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
package edu.dfci.cccb.mev.dataset.domain.simple;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractAnalyses;

/**
 * @author levk
 * 
 */
@Log4j
public class ArrayListAnalyses extends AbstractAnalyses {

  private List<Analysis> analyses = new ArrayList<> ();

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Analyses#put(edu.dfci.cccb.mev
   * .dataset.domain.contract.Analysis) */
  @Override
  public void put (Analysis analysis) {
    analyses.add (0, analysis);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Analyses#get(java.lang.String) */
  @Override
  public Analysis get (String name) throws AnalysisNotFoundException {
    for (Analysis analysis : analyses)
      if (analysis.name ().equals (name)) {
        if (log.isDebugEnabled ())
          log.debug ("Found analysis " + name + " of type " + analysis.getClass ().getName ());
        return analysis;
      }
    throw new AnalysisNotFoundException ().name (name);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.Analyses#remove(java.lang.String) */
  @Override
  public void remove (String name) throws AnalysisNotFoundException {
    for (Iterator<Analysis> analyses = this.analyses.iterator (); analyses.hasNext ();)
      if (analyses.next ().name ().equals (name)) {
        analyses.remove ();
        return;
      }
    throw new AnalysisNotFoundException ().name (name);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.Analyses#list() */
  @Override
  public List<String> list () {
    return new AbstractList<String> () {

      @Override
      public String get (int index) {
        return analyses.get (index).name ();
      }

      @Override
      public int size () {
        return analyses.size ();
      }
    };
  }
}
