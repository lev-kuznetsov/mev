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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import lombok.extern.log4j.Log4j;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import edu.dfci.cccb.mev.dataset.domain.fs.IFlatFileValues;

/**
 * @author levk
 * 
 */
@Log4j
public class FlatFileValuesBinary32FloatMessageConverter extends AbstractHttpMessageConverter<IFlatFileValues> {

  /**
   * 
   */
  public FlatFileValuesBinary32FloatMessageConverter () {
    super (MediaType.APPLICATION_OCTET_STREAM);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#supports
   * (java.lang.Class) */
  @Override
  protected boolean supports (Class<?> clazz) {
    return IFlatFileValues.class.isAssignableFrom (clazz);
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#readInternal
   * (java.lang.Class, org.springframework.http.HttpInputMessage) */
  @Override
  protected IFlatFileValues readInternal (Class<? extends IFlatFileValues> clazz, HttpInputMessage inputMessage) throws IOException,
                                                                                                      HttpMessageNotReadableException {
    throw new UnsupportedOperationException ();
  }

  /* (non-Javadoc)
   * @see
   * org.springframework.http.converter.AbstractHttpMessageConverter#writeInternal
   * (java.lang.Object, org.springframework.http.HttpOutputMessage) */
  @Override
  protected void writeInternal (IFlatFileValues t, HttpOutputMessage outputMessage) throws IOException,
                                                                              HttpMessageNotWritableException {
   
    try(DataOutputStream out = new DataOutputStream (outputMessage.getBody ())){
      try(DataInputStream in = new DataInputStream (t.asInputStream ())){
        while(true){          
          double dval = in.readDouble ();
          float fval = (float) dval;
          out.writeFloat (fval);
        }
      }catch(EOFException e){
        log.debug("Finished serializing IFlatFileValues");
      }
    }   
  }
}
