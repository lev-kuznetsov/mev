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

import static java.util.stream.Collectors.toMap;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import edu.dfci.cccb.mev.dataset.Dataset;

/**
 * Rserve dataset serializer
 * 
 * @author levk
 */
public class RserveDatasetSerializer extends JsonSerializer <Dataset> {

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider)
   */
  @Override
  public void serialize (Dataset value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException, JsonProcessingException {
    serializers.defaultSerializeValue (value.values ().query (value.dimensions ().entrySet ().stream ().collect (toMap (e -> e.getKey (),
                                                                                                                        e -> e.getValue ().keys ()))),
                                       gen);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.databind.JsonSerializer#serializeWithType(java.lang.
   * Object, com.fasterxml.jackson.core.JsonGenerator,
   * com.fasterxml.jackson.databind.SerializerProvider,
   * com.fasterxml.jackson.databind.jsontype.TypeSerializer)
   */
  @Override
  public void serializeWithType (Dataset value, JsonGenerator gen, SerializerProvider serializers,
                                 TypeSerializer typeSer)
      throws IOException {
    serialize (value, gen, serializers);
  }
}
