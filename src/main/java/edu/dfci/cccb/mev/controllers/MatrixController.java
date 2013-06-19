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
package edu.dfci.cccb.mev.controllers;

import static java.lang.Math.min;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.log4j.Log4j;

import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import us.levk.math.linear.HugeRealMatrix;
import us.levk.math.linear.util.RealMatrixJsonSerializer;

/**
 * @author levk
 * 
 */
@Controller
@RequestMapping ("/matrix")
@SessionAttributes ("matrices")
@Log4j
public class MatrixController {

  @RequestMapping (value = "/{id}", method = GET)
  @ResponseBody
  @JsonSerialize (using = RealMatrixJsonSerializer.class)
  public RealMatrix get (@ModelAttribute ("matrices") Map<String, RealMatrix> matrices,
                         @PathVariable ("id") String id,
                         @RequestParam (value = "row", required = false) Integer row,
                         @RequestParam (value = "column", required = false) Integer column,
                         @RequestParam (value = "rows", required = false) Integer rows,
                         @RequestParam (value = "columns", required = false) Integer columns) {
    RealMatrix result = matrices.get (id);
    if (log.isDebugEnabled ())
      log.debug ("Getting matrix " + id + " from " + matrices + " found " + result);
    if (result != null && row != null && column != null) {
      if (row >= result.getRowDimension () || column >= result.getColumnDimension ())
        return null;
      if (rows == null || columns == null)
        rows = columns = 1;
      return result.getSubMatrix (row,
                                  min (row + rows, result.getRowDimension () - 1),
                                  column,
                                  min (column + columns, result.getColumnDimension () - 1));
    }
    return result;
  }

  @RequestMapping (value = "/{id}", method = POST)
  @ResponseStatus (OK)
  public void put (@ModelAttribute ("matrices") Map<String, RealMatrix> matrices,
                   @PathVariable ("id") String id,
                   @RequestParam MultipartFile file) throws IOException, ParseException {
    try (InputStream in = file.getInputStream ()) {
      matrices.put (id, new HugeRealMatrix (in,
                                            "\t,".toCharArray (),
                                            "\n".toCharArray (),
                                            NumberFormat.getNumberInstance ()));
      log.debug ("Added /matrix/" + id);
    }
  }

  @ModelAttribute ("matrices")
  public Map<String, RealMatrix> createMatrixMap () {
    return new HashMap<String, RealMatrix> ();
  }
}
