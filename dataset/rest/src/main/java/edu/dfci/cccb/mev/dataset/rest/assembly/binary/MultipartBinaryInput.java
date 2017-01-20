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
package edu.dfci.cccb.mev.dataset.rest.assembly.binary;

import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractRawInput;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author levk
 * 
 */
@ToString
public class MultipartBinaryInput extends AbstractRawInput {

  private MultipartFile file;

  /**
   *
   */
  public MultipartBinaryInput(MultipartFile file) {
    this.file = file;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#contentType() */
  @Override
  public String contentType () {
    return "binary64";
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#name() */
  @Override
  public String name () {
    return file.getOriginalFilename ();
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#input() */
  @Override
  public InputStream input () throws IOException {
    return file.getInputStream ();
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#size() */
  @Override
  public long size () {
    return file.getSize ();
  }
}
