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
package edu.dfci.cccb.mev.dataset.rest.controllers;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;

/**
 * @author levk
 * 
 */
@ControllerAdvice
public class DatasetAdvice {

  @ExceptionHandler ({ AnalysisNotFoundException.class,
                      DatasetNotFoundException.class,
                      SelectionNotFoundException.class })
  @ResponseBody
  @ResponseStatus (NOT_FOUND)
  public DatasetException resourceNotFound (DatasetException e) {
    return e;
  }

  @ExceptionHandler (DatasetBuilderException.class)
  @ResponseBody
  @ResponseStatus (UNSUPPORTED_MEDIA_TYPE)
  public DatasetException unsupportedMediaType (DatasetException e) {
    return e;
  }

  @ExceptionHandler ({ InvalidCoordinateException.class,
                      InvalidDimensionTypeException.class,
                      InvalidDatasetNameException.class })
  @ResponseBody
  @ResponseStatus (UNPROCESSABLE_ENTITY)
  public DatasetException unableToProcess (DatasetException e) {
    return e;
  }
}
