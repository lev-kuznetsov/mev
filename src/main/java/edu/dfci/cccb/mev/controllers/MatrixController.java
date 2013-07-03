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
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import edu.dfci.cccb.mev.beans.Matrices;
import edu.dfci.cccb.mev.beans.MatrixNotFoundException;

/**
 * Matrix controller
 * 
 * @author levk
 * 
 */
@Controller
@RequestMapping ("/matrix")
// TODO: Move this off into a separate module
public class MatrixController {

  @Autowired private Matrices matrices;

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
  public RealMatrix matrix (@PathVariable ("matrix") String matrix,
                            @RequestParam (value = "row", required = false) Integer row,
                            @RequestParam (value = "column", required = false) Integer column,
                            @RequestParam (value = "rows", required = false) Integer rows,
                            @RequestParam (value = "columns", required = false) Integer columns) throws MatrixNotFoundException {
    RealMatrix result = matrices.get (matrix);
    if (row != null && column != null) {
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
  @ResponseStatus (OK)
  public void put (@PathVariable ("matrix") String matrix,
                   @RequestParam ("file") MultipartFile file) throws IOException, ParseException {
    try (InputStream in = file.getInputStream ()) {
      matrices.put (matrix, in);
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
  public void delete (@PathVariable ("matrix") String matrix) throws MatrixNotFoundException, IOException {
    matrices.delete (matrix);
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
   * @throws InterruptedException
   */
  @RequestMapping (method = POST)
  @ResponseBody
  public String add (@RequestParam ("file") MultipartFile file) throws IOException,
                                                               ParseException,
                                                               InterruptedException {
    try (InputStream in = file.getInputStream ()) {
      return matrices.add (file.getName (), in);
    }
  }

  /**
   * Gets the names of all matrices available for this session
   * 
   * @param matrices
   * @return
   */
  @RequestMapping (method = GET)
  @ResponseBody
  public Collection<String> matrices () {
    return matrices.list ();
  }

  /**
   * Gets all the decoration types for a particular matrix, resultant JSON
   * object is an array of arrays sorted by dimension
   * 
   * @param matrix
   * @return
   * @throws MatrixNotFoundException
   */
  @RequestMapping (value = "/{matrix}/decoration", method = GET)
  @ResponseBody
  public Collection<Collection<String>> decorations (@PathVariable ("matrix") String matrix) throws MatrixNotFoundException {
    return matrices.decorations (matrix);
  }

  /**
   * Decorates a particular vector
   * 
   * @param matrix
   * @param dimension
   * @param index
   * @param name
   * @param decoration
   * @throws MatrixNotFoundException
   */
  @RequestMapping (value = "/{matrix}/decoration/{dimension}/{index}/{name}", method = PUT)
  @ResponseBody
  public void decorate (@PathVariable ("matrix") String matrix,
                        @PathVariable ("dimension") String dimension,
                        @PathVariable ("index") int index,
                        @PathVariable ("name") String name,
                        @RequestParam ("decoration") String decoration) throws MatrixNotFoundException {
    matrices.decorate (matrix, toDimension (dimension), name, index, decoration);
  }

  /**
   * Gets a specific decoration given the attributes
   * 
   * @param matrix
   * @param dimension
   * @param index
   * @param name
   * @return
   * @throws MatrixNotFoundException
   */
  @RequestMapping (value = "/{matrix}/decoration/{dimension}/{index}/{name}", method = GET)
  @ResponseBody
  public String decoration (@PathVariable ("matrix") String matrix,
                            @PathVariable ("dimension") String dimension,
                            @PathVariable ("index") int index,
                            @PathVariable ("name") String name) throws MatrixNotFoundException {
    return matrices.decoration (matrix, toDimension (dimension), index, name);
  }

  /**
   * Handles calls to non-existent matrix URLs
   * 
   * @param e
   * @return
   */
  @ExceptionHandler (MatrixNotFoundException.class)
  @ResponseStatus (value = NOT_FOUND, reason = "Bad matrix key")
  public void handleNotFound (MatrixNotFoundException e, Locale locale) {}

  /**
   * Handles bad matrix data files
   * 
   * @param e
   * @param locale
   */
  @ExceptionHandler (ParseException.class)
  @ResponseStatus (value = UNSUPPORTED_MEDIA_TYPE, reason = "Bad matrix data")
  public void handleParseFailure (ParseException e, Locale locale) {}

  /**
   * Converts from string to a particular dimension for decorations, allowed
   * values are "row", "column", or a numerical representation.
   * 
   * @param dimension
   * @return
   */
  private int toDimension (String dimension) {
    if ("row".equals (dimension))
      return 0;
    else if ("column".equals (dimension))
      return 1;
    else
      return Integer.parseInt (dimension);
  }
}
