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

package edu.dfci.cccb.mev.googleplus.services.controllers;

import static edu.dfci.cccb.mev.googleplus.domain.support.Mixins.execute;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.CommentList;

import edu.dfci.cccb.mev.common.domain.support.MevException;

/**
 * @author levk
 * @since CRYSTAL
 */
@Path ("/google/comment")
public class CommentsController {

  private @Inject Provider<Drive> drive;

  @GET
  @Produces (APPLICATION_JSON)
  public CommentList list (@QueryParam ("file") String file,
                           @QueryParam ("size") @DefaultValue ("20") int size) throws IOException, MevException {
    return execute (drive.get ().comments ().list (file).setMaxResults (size));
  }

  @GET
  @Produces (APPLICATION_JSON)
  public CommentList list (@QueryParam ("file") String file,
                           @QueryParam ("page") String page) throws IOException, MevException {
    return execute (drive.get ().comments ().list (file).setPageToken (page));
  }
}
