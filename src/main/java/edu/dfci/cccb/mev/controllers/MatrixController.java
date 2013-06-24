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
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.extern.log4j.Log4j;

import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import us.levk.math.linear.HugeRealMatrix;

/**
 * Matrix controller
 * 
 * @author levk
 * 
 */
@Controller
@RequestMapping ("/matrix")
@SessionAttributes ({ "matrices" })
@Log4j
public class MatrixController {

  /**
   * Gets a matrix (or submatrix thereof) under specified path.
   * 
   * If the optional parameters are specified and are out of bounds, as large a
   * matrix as possible is returned.
   * 
   * @param matrices
   * @param matrix
   * @param row Optional, starting from this row
   * @param column Optional, starting from this column
   * @param rows Optional, number of rows
   * @param columns Optional, number of columns
   * @return
   * @throws MatrixNotFoundException
   */
  @RequestMapping (value = "/{matrix}", method = GET)
  @ResponseBody
  public RealMatrix matrix (@ModelAttribute ("matrices") Map<String, RealMatrix> matrices,
                            @PathVariable ("matrix") String matrix,
                            @RequestParam (value = "row", required = false) Integer row,
                            @RequestParam (value = "column", required = false) Integer column,
                            @RequestParam (value = "rows", required = false) Integer rows,
                            @RequestParam (value = "columns", required = false) Integer columns) throws MatrixNotFoundException {
    RealMatrix result = matrices.get (matrix);
    if (result == null)
      throw new MatrixNotFoundException (matrix);
    else if (row != null && column != null) {
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

  /**
   * Uploads a matrix under a specific path
   * 
   * @param matrices
   * @param matrix
   * @param file
   * @return
   * @throws IOException
   * @throws ParseException
   */
  @RequestMapping (value = "/{matrix}", method = PUT)
  @ResponseBody
  public String put (@ModelAttribute ("matrices") Map<String, RealMatrix> matrices,
                     @PathVariable ("matrix") String matrix,
                     @RequestParam MultipartFile file) throws IOException, ParseException {
    try (InputStream in = file.getInputStream ()) {
      matrices.put (matrix, new HugeRealMatrix (in,
                                                "\t,".toCharArray (),
                                                "\n".toCharArray (),
                                                NumberFormat.getNumberInstance ()));
      log.debug ("Added /matrix/" + matrix);
      return matrix;
    }
  }

  /**
   * Deletes a matrix under a specific path
   * 
   * @param matrices
   * @param matrix
   * @throws MatrixNotFoundException
   */
  @RequestMapping (value = "/{matrix}", method = DELETE)
  @ResponseStatus (OK)
  public void delete (@ModelAttribute ("matrices") Map<String, RealMatrix> matrices,
                      @PathVariable ("matrix") String matrix) throws MatrixNotFoundException {
    if (null == matrices.remove (matrix))
      throw new MatrixNotFoundException (matrix);
  }

  /**
   * Uploads a matrix to a default path of the filename; if a matrix under that
   * path already exists the name will be suffixed with integers
   * 
   * @param matrices
   * @param file
   * @return
   * @throws IOException
   * @throws ParseException
   */
  @RequestMapping (method = POST)
  @ResponseBody
  public String add (@ModelAttribute ("matrices") Map<String, RealMatrix> matrices,
                     @RequestParam MultipartFile file) throws IOException, ParseException {
    String name = file.getName ();
    if (matrices.containsKey (name)) {
      int suffix = 1;
      for (; matrices.containsKey (name + "_" + suffix); suffix++);
      name = name + "_" + suffix;
    }
    return put (matrices, name, file);
  }

  /**
   * Gets the names of all matrices available for this session
   * 
   * @param matrices
   * @return
   */
  @RequestMapping (method = GET)
  @ResponseBody
  public Collection<String> matrices (@ModelAttribute ("matrices") Map<String, RealMatrix> matrices) {
    return matrices.keySet ();
  }

  /**
   * Creates a new matrix map for the session
   * 
   * @return
   */
  @ModelAttribute ("matrices")
  public Map<String, RealMatrix> createMatrixMap () {
    return new HashMap<String, RealMatrix> ();
  }

  /**
   * Handles calls to non-existant matrix URLs
   * 
   * @param e
   * @return
   */
  @ExceptionHandler (MatrixNotFoundException.class)
  @ResponseStatus (value = NOT_FOUND, reason = "Non-existant matrix key")
  @ResponseBody
  public String handleNotFound (MatrixNotFoundException e, Locale locale) {
    return e.getLocalizedMessage ();
  }
}
