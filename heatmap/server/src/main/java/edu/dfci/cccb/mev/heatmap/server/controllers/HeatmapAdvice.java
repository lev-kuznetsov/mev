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
package edu.dfci.cccb.mev.heatmap.server.controllers;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.dfci.cccb.mev.heatmap.domain.AnnotationBuilderException;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapBuilderException;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapException;
import edu.dfci.cccb.mev.heatmap.domain.HeatmapNotFoundException;
import edu.dfci.cccb.mev.heatmap.domain.InvalidDimensionException;
import edu.dfci.cccb.mev.heatmap.domain.InvalidHeatmapNameException;

/**
 * @author levk
 * 
 */
@ControllerAdvice
public class HeatmapAdvice {

  @ExceptionHandler (HeatmapNotFoundException.class)
  @ResponseStatus (NOT_FOUND)
  @ResponseBody
  public HeatmapException handleHeatmapNotFound (HeatmapNotFoundException e) {
    return e;
  }

  @ExceptionHandler ({ AnnotationBuilderException.class, HeatmapBuilderException.class })
  @ResponseStatus (UNSUPPORTED_MEDIA_TYPE)
  @ResponseBody
  public HeatmapException handleBadMedia (HeatmapException e) {
    return e;
  }

  @ExceptionHandler (InvalidDimensionException.class)
  @ResponseStatus (REQUESTED_RANGE_NOT_SATISFIABLE)
  @ResponseBody
  public HeatmapException handleBadDimension (InvalidDimensionException e) {
    return e;
  }

  @ExceptionHandler (InvalidHeatmapNameException.class)
  @ResponseStatus (UNPROCESSABLE_ENTITY)
  @ResponseBody
  public HeatmapException handleBadHeatmapName (InvalidHeatmapNameException e) {
    return e;
  }
}
