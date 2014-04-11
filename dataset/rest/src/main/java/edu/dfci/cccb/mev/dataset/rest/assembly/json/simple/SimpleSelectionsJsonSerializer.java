package edu.dfci.cccb.mev.dataset.rest.assembly.json.simple;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.dataset.domain.contract.Selections;

public class SimpleSelectionsJsonSerializer extends JsonSerializer<Selections> {

  @Override
  public Class<Selections> handledType () {
    return Selections.class;
  }
  
  @Override
  public void serialize (Selections value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                                                                                           JsonProcessingException {
    provider.defaultSerializeValue (value.getAll (), jgen);
  }
}
