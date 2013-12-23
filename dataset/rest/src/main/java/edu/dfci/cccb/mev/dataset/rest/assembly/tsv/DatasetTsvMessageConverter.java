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
package edu.dfci.cccb.mev.dataset.rest.assembly.tsv;

import java.io.IOException;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import edu.dfci.cccb.mev.dataset.domain.contract.ComposerFactory;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetComposingException;
import edu.dfci.cccb.mev.dataset.rest.assembly.tsv.prototype.AbstractTsvHttpMessageConverter;

/**
 * @author levk
 * 
 */
@ToString
public class DatasetTsvMessageConverter extends AbstractTsvHttpMessageConverter<Dataset> {

  private @Getter @Setter (onMethod = @_ (@Inject)) ComposerFactory composer;

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#supports
   * (java.lang.Class) */
  @Override
  protected boolean supports (Class<?> clazz) {
    return Dataset.class.isAssignableFrom (clazz);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#readInternal
   * (java.lang.Class, org.springframework.http.HttpInputMessage) */
  @Override
  protected Dataset readInternal (Class<? extends Dataset> clazz, final HttpInputMessage inputMessage) throws IOException,
                                                                                                      HttpMessageNotReadableException {
    throw new UnsupportedOperationException ("nyi");
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#writeInternal
   * (java.lang.Object, org.springframework.http.HttpOutputMessage) */
  @Override
  protected void writeInternal (Dataset t, HttpOutputMessage outputMessage) throws IOException,
                                                                           HttpMessageNotWritableException {
    try {
      composer.compose (t).write (outputMessage.getBody ());
    } catch (DatasetComposingException e) {
      HttpMessageNotWritableException exception = new HttpMessageNotWritableException ("Unable to compose dataset "
                                                                                       + t.name ());
      exception.initCause (e);
      throw exception;
    }
  }
}
