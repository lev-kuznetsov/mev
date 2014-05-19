package edu.dfci.cccb.mev.anova.rest.assembly.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.dfci.cccb.mev.anova.domain.contract.Anova.Entry;


public class EntryJsonSerializer extends JsonSerializer<Entry>{

  
  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonSerializer#handledType() */
  @Override
  public Class<Entry> handledType () {
    return Entry.class;
  }
  
  @Override
  public void serialize (Entry value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
  JsonProcessingException {
    jgen.writeStartObject ();
    jgen.writeStringField ("id", value.geneId ());
    jgen.writeNumberField ("pValue", value.pValue ());
    jgen.writeArrayFieldStart ("pairwise_log_fold_change");
    for(Entry.Pairing pairing:value.logFoldChanges ().keySet ()){
      jgen.writeStartObject ();
      jgen.writeStringField ("partnerA", pairing.partnerA ());
      jgen.writeStringField ("partnerB", pairing.partnerB ());
      jgen.writeNumberField ("ratio", value.logFoldChanges ().get(pairing));
      jgen.writeEndObject ();
    }
    jgen.writeEndArray ();
    jgen.writeEndObject ();
}

}
