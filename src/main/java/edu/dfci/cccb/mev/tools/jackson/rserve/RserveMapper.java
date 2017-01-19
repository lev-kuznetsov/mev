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
package edu.dfci.cccb.mev.tools.jackson.rserve;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.dfci.cccb.mev.dataset.Dataset;
import edu.dfci.cccb.mev.tools.jackson.CdiInjectionHandler;

/**
 * Rserve protocol mapper
 * 
 * @author levk
 */
@Singleton
public class RserveMapper extends ObjectMapper {

  /**
   * Serialization details
   */
  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public RserveMapper () {
    this (new RserveFactory ());
  }

  /**
   * @param jf
   */
  public RserveMapper (RserveFactory jf) {
    super (jf);
    setInjectableValues (new CdiInjectionHandler ());
    enable (ACCEPT_SINGLE_VALUE_AS_ARRAY);
    registerModule (new SimpleModule () {
      private static final long serialVersionUID = 1L;

      {
        addSerializer (Dataset.class, new RserveDatasetSerializer ());
        addDeserializer (Dataset.class, new RserveDatasetDeserializer ());
      }
    });
  }

  /**
   * @param src
   */
  public RserveMapper (RserveMapper src) {
    super (src);
  }

  @Override
  public RserveMapper copy () {
    return new RserveMapper (this);
  }

  @Override
  public RserveFactory getFactory () {
    return (RserveFactory) _jsonFactory;
  }
}
