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

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import edu.dfci.cccb.mev.dataset.Dataset;
import edu.dfci.cccb.mev.dataset.literal.Literal;

/**
 * Rserve protocol dataset deserializer
 * 
 * @author levk
 */
public class RserveDatasetDeserializer extends JsonDeserializer <Dataset> {

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.
   * jackson.core.JsonParser,
   * com.fasterxml.jackson.databind.DeserializationContext)
   */
  @Override
  public Dataset deserialize (JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    Dataset d = new Literal ();
    d.values ().bind (p.readValueAs (new TypeReference <Map <String, Map <String, Double>>> () {}));
    return d;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.databind.JsonDeserializer#deserializeWithType(com.
   * fasterxml.jackson.core.JsonParser,
   * com.fasterxml.jackson.databind.DeserializationContext,
   * com.fasterxml.jackson.databind.jsontype.TypeDeserializer)
   */
  @Override
  public Object deserializeWithType (JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
      throws IOException {
    return deserialize (p, ctxt);
  }
}
