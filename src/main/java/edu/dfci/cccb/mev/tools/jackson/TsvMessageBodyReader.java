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
package edu.dfci.cccb.mev.tools.jackson;

import static java.util.Spliterator.CONCURRENT;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;
import static javax.ws.rs.core.MediaType.valueOf;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * TSV input reader
 * 
 * @author levk
 */
@Consumes ("text/tab-separated-values")
public interface TsvMessageBodyReader <T> extends MessageBodyReader <T> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType)
   */
  @Override
  default boolean isReadable (Class <?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return valueOf ("text/tab-separated-values").isCompatible (mediaType);
  }

  /**
   * @param r
   * @param in
   * @return map
   * @throws IOException
   */
  default <V> Map <String, Map <String, V>> readFrom (ObjectReader r, InputStream in) throws IOException {
    MappingIterator <Map <String, V>> i = r.readValues (in);
    Stream <Map <String, V>> s = stream (spliteratorUnknownSize (i, CONCURRENT), true);
    return s.collect (toMap (e -> {
      return (String) e.values ().iterator ().next ();
    }, e -> {
      Map <String, V> m = new HashMap <> ();
      Iterator <Entry <String, V>> l = e.entrySet ().iterator ();
      l.next ();
      l.forEachRemaining (c -> m.put (c.getKey (), c.getValue ()));
      return m;
    }));
  }
}
