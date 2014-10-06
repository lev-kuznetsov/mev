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

package edu.dfci.cccb.mev.dataset.domain.prototype;

import static edu.dfci.cccb.mev.dataset.domain.prototype.AnalysisAdapter.analyses;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.Analysis;

public class AnalysisAdapterTest {

  private static class MockAnalysis extends AnalysisAdapter {
    public void run () {}
  }

  private Analysis mock1 = new MockAnalysis ().name ("mock");
  private Analysis mock2 = new MockAnalysis ().name ("mock2");
  private Analysis mock3 = new MockAnalysis ().name ("mock");
  private Map<String, Analysis> analyses = analyses ();

  @Test
  public void name () throws Exception {
    assertThat (mock1.name (), is ("mock"));
  }

  @Test
  public void empty () throws Exception {
    assertThat (analyses.isEmpty (), is (true));
  }

  @Test
  public void putAndGet () throws Exception {
    analyses.put ("mock", mock1);
    analyses.put ("mock2", mock2);
    assertEquals (analyses.get ("mock"), mock1);
    assertEquals (analyses.get ("mock2"), mock2);
    assertThat (analyses.size (), is (2));
  }

  @Test
  public void sameNamePutAndGet () throws Exception {
    analyses.put ("mock", mock1);
    assertEquals (analyses.get ("mock"), mock1);
    assertThat (analyses.size (), is (1));
    analyses.put ("mock", mock3);
    assertEquals (analyses.get ("mock"), mock3);
    assertThat (analyses.size (), is (1));
  }
}
