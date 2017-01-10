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
package edu.dfci.cccb.mev.tools;

import static javax.ws.rs.core.MediaType.valueOf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * @author levk
 */
@Produces ("text/tab-separated-values")
@Consumes ("text/tab-separated-values")
public abstract class TsvAdapter <T> implements MessageBodyReader <T>, MessageBodyWriter <T> {

  public abstract <V> Map <String, V> asMap (T v);

  public abstract T asType (Map <String, String> m);

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType)
   */
  @Override
  public boolean isWriteable (Class <?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
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
  public long getSize (T t, Class <?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object,
   * java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.OutputStream)
   */
  @Override
  public void writeTo (T t, Class <?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                       MultivaluedMap <String, Object> httpHeaders, OutputStream entityStream)
      throws IOException, WebApplicationException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType)
   */
  @Override
  public boolean isReadable (Class <?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return valueOf ("text/tab-separated-values").isCompatible (mediaType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.InputStream)
   */
  @Override
  public T readFrom (Class <T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                     MultivaluedMap <String, String> httpHeaders, InputStream entityStream)
      throws IOException, WebApplicationException {
    // TODO Auto-generated method stub
    return null;
  }

}
