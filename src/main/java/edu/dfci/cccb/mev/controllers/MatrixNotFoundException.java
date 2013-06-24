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

import lombok.Getter;

/**
 * This exception is thrown by REST methods when a specified URL doesn't have
 * a matrix associated with it
 * 
 * @author levk
 * 
 */
public class MatrixNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  private final @Getter String matrix;

  public MatrixNotFoundException (String matrix) {
    super ("Could not find matrix " + matrix + " in the session");
    this.matrix = matrix;
  }
}