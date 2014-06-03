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
package edu.dfci.cccb.mev.dataset.domain.mock;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractRawInput;

/**
 * @author levk
 * 
 */
@EqualsAndHashCode (callSuper = true)
@ToString
public class MockTsvInput extends AbstractRawInput {

  private final String name;
  private final String stringContent;
  private final File fileContent;

  /**
   * 
   */
  public MockTsvInput (String name, String content) {
    this.name = name;
    this.stringContent = content;
    fileContent = null;
  }

  public MockTsvInput (String name, File content) {
    this.name = name;
    fileContent = content;
    stringContent = null;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#contentType() */
  @Override
  public String contentType () {
    return TAB_SEPARATED_VALUES;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#name() */
  @Override
  public String name () {
    return name;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#input() */
  @Override
  public InputStream input () throws IOException {
    if (stringContent != null)
      return new ByteArrayInputStream (stringContent.getBytes ());
    else if (fileContent != null)
      return new FileInputStream (fileContent);
    else
      throw new IllegalStateException ();
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#size() */
  @Override
  public long size () {
    if (stringContent != null)
      return stringContent.length ();
    else if (fileContent != null)
      return (int) fileContent.length ();
    else
      throw new IllegalStateException ();
  }
}
