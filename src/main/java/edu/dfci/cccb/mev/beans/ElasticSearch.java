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
package edu.dfci.cccb.mev.beans;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.fabric8.cdi.Services;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
 * ElasticSearch
 * 
 * @author levk
 */
@Stateless
public class ElasticSearch {

  /**
   * @param es
   * @return es client
   */
  @Produces
  @Singleton
  static JestClient es () {
    // Workaround, see https://github.com/fabric8io/fabric8/issues/6699
    String endpoint = Services.toServiceUrl ("elasticsearch", "http", "http", null, false);
    // endpoint = endpoint.replaceAll ("tcp", "http");

    JestClientFactory factory = new JestClientFactory ();
    factory.setHttpClientConfig (new HttpClientConfig.Builder (endpoint).multiThreaded (true).build ());

    return factory.getObject ();
  }
}
