/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.common.services.support.mappers;

import static javax.ws.rs.core.Response.serverError;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

import edu.dfci.cccb.mev.common.domain.support.MevException;

/**
 * @author levk
 * @since CRYSTAL
 */
public class MevExceptionMapper implements ExceptionMapper<MevException> {

  /* (non-Javadoc)
   * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable) */
  @Override
  @Produces ("application/json")
  public Response toResponse (MevException exception) {
    return toResponseBuilder (exception).entity (toEntity (exception)).build ();
  }

  protected <T extends MevException> ResponseBuilder toResponseBuilder (T exception) {
    return serverError ();
  }

  protected <T extends MevException> Object toEntity (T exception) {
    return exception;
  }
}
