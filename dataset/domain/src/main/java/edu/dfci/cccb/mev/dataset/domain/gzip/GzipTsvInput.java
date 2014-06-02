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
package edu.dfci.cccb.mev.dataset.domain.gzip;

import static org.apache.commons.io.FilenameUtils.getBaseName;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractRawInput;
import edu.dfci.cccb.mev.io.implementation.TemporaryFile;

/**
 * @author levk
 * 
 */
@Log4j
public class GzipTsvInput extends AbstractRawInput {

  private URL url;
  final int BUFFER = 2048;

  public GzipTsvInput (URL url) {
    if (url == null)
      throw new NullPointerException ("URL cannot be null");
    this.url = url;
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
    return super.name () != null ? super.name () : getBaseName (url.toString ());
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#input() */
  @Override
  public InputStream input () throws IOException {
    if (log.isDebugEnabled ())
      log.debug (url);

    GZIPInputStream zis = null;
    BufferedOutputStream dest = null;
    BufferedInputStream bis = null;
    File unzipped = new TemporaryFile ();
    try {
      URLConnection urlc = url.openConnection ();
      zis = new GZIPInputStream (urlc.getInputStream ());

      int count;
      byte data[] = new byte[BUFFER];
      // write the files to the disk

      FileOutputStream fos = new FileOutputStream (unzipped);
      if (log.isDebugEnabled ())
        log.debug ("Unzipping SOFT file to " + unzipped.getAbsolutePath ());

      dest = new BufferedOutputStream (fos, BUFFER);
      while ((count = zis.read (data, 0, BUFFER)) != -1) {
        dest.write (data, 0, count);
      }
      dest.flush ();
      dest.close ();
      return new FileInputStream (unzipped);
      
    } finally {
      if (bis != null) {
        try {
          bis.close ();
        } catch (IOException ioe) {}
      }
      if (dest != null) {
        try {
          dest.close ();
        } catch (IOException ioe) {}
      }
      if (zis != null) {
        try {
          zis.close ();
        } catch (IOException ioe) {}
      }
    }
    
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.RawInput#size() */
  @Override
  public long size () {
    return -1;
  }
}
