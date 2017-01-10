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

/**
 * Analysis state
 * 
 * @author levk
 */
public enum State {
  // Order matters, state is persisted as ordinal (see Analysis.state field
  // @Enumerated settings). Any additions here will render any present data
  // incompatible

  /**
   * Analysis has not been started
   */
  PENDING,
  /**
   * Signal the start of analysis and transition into the {@link #RUNNING}
   */
  STARTING,
  /**
   * Analysis is currently in progess
   */
  RUNNING,
  /**
   * Analysis has successfully completed
   */
  COMPLETED,
  /**
   * Analysis had errors
   */
  FAILED;

  /**
   * @return <tt>true</tt> is the state is terminal
   */
  public boolean isTerminal () {
    return this == COMPLETED || this == FAILED;
  }
}
