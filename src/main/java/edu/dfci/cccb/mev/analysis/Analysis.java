/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Dana-Farber Cancer Institute
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package edu.dfci.cccb.mev.analysis;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static edu.dfci.cccb.mev.analysis.State.PENDING;
import static edu.dfci.cccb.mev.analysis.State.RUNNING;
import static edu.dfci.cccb.mev.analysis.State.STARTING;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.workspace.Item;

/**
 * Generic analysis logic
 * 
 * @author levk
 */
@MappedSuperclass
public abstract class Analysis extends Item {

  /**
   * State
   */
  private @Enumerated @Column State state = PENDING;
  /**
   * Error
   */
  private @Error @Column String error;

  /**
   * @return state
   */
  @JsonProperty
  @Path ("state")
  @GET
  public State state () {
    return state;
  }

  /**
   * @param target
   *          state
   */
  @JsonProperty ("state")
  @Path ("state")
  @PUT
  public synchronized void transition (State target) {
    if ((state == PENDING && target != RUNNING) || (state == STARTING && target == RUNNING)
        || (state == RUNNING && target.isTerminal ()))
      state = target;
    else throw new BadRequestException ("Invalid transition from " + state + " to " + target);
  }

  /**
   * @return error
   */
  @Path ("error")
  @GET
  @JsonProperty ("error")
  @JsonInclude (NON_EMPTY)
  public String error () {
    return error;
  }
}
