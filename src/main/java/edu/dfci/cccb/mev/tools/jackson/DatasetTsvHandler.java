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

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import edu.dfci.cccb.mev.dataset.Dataset;
import edu.dfci.cccb.mev.dataset.literal.Literal;
import edu.dfci.cccb.mev.workspace.Item;

/**
 * Dataset TSV handler. Note that the reader type is {@link Item}, anything
 * uploaded to an item slot with TSV content header is assumed to be a dataset
 * 
 * @author levk
 */
@Provider
public class DatasetTsvHandler implements TsvMessageBodyReader <Item>, TsvMessaageBodyWriter <Dataset> {
  /**
   * Schema
   */
  private @Inject CsvSchema schema;
  /**
   * Mapper
   */
  private @Inject CsvMapper mapper;
  /**
   * Dataset type
   */
  private final TypeReference <Map <String, Map <String, Double>>> type =
      new TypeReference <Map <String, Map <String, Double>>> () {};

  /*
   * (non-Javadoc)
   * 
   * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.InputStream)
   */
  @Override
  public Item readFrom (Class <Item> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap <String, String> httpHeaders, InputStream in)
      throws IOException, WebApplicationException {
    Literal d = new Literal ();
    d.bind (readFrom (mapper.readerFor (this.type).with (schema), in));
    return d;
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
  public void writeTo (Dataset t, Class <?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                       MultivaluedMap <String, Object> httpHeaders, OutputStream out)
      throws IOException, WebApplicationException {
    writeTo (mapper.writerFor (this.type).with (schema),
             t.values ().query (t.dimensions ().entrySet ().stream ().collect (toMap (e -> e.getKey (),
                                                                                      e -> e.getValue ().keys ()))),
             out);
  }
}
