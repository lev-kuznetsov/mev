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
package edu.dfci.cccb.mev.tools.jackson.tsv;

import static javax.ws.rs.core.MediaType.valueOf;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyWriter;

import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * TSV message writer
 * 
 * @author levk
 */
@Produces ("text/tab-separated-values")
public interface TsvMessaageBodyWriter <T> extends MessageBodyWriter <T> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType)
   */
  @Override
  default boolean isWriteable (Class <?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return valueOf ("text/tab-separated-values").isCompatible (mediaType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object,
   * java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType)
   */
  @Override
  default long getSize (T t, Class <?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  /**
   * @param t
   * @param out
   * @throws IOException
   */
  default <V> void writeTo (ObjectWriter w, Map <String, Map <String, V>> t, OutputStream out) throws IOException {
    w.writeValues (out).writeAll (t.entrySet ().stream ().map (e -> {
      LinkedHashMap <String, Object> m = new LinkedHashMap <> ();
      m.put ("", e.getKey ());
      m.putAll (e.getValue ());
      return m;
    }).toArray ());
  }
}
