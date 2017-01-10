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
package edu.dfci.cccb.mev.context;

import static java.lang.Integer.valueOf;

import javax.enterprise.inject.Produces;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import io.fabric8.annotations.ServiceName;

/**
 * R context
 * 
 * @author levk
 */
public class R {

  /**
   * @param endpoint
   *          rserve service endpoint
   * @return Rserve connection
   * @throws RserveException
   */
  @Produces
  static RConnection r (@ServiceName ("mev-rserve") String endpoint) throws RserveException {
    return new RConnection (endpoint.substring (endpoint.lastIndexOf ('/') + 1, endpoint.lastIndexOf (':')),
                            valueOf (endpoint.substring (endpoint.lastIndexOf (':') + 1))) {
      @Override
      public boolean close () {
        return super.close ();
      }
    };
  }
}
