package edu.dfci.cccb.mev.kmeans.domain.contract;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;

import edu.dfci.cccb.mev.kmeans.domain.contract.Cluster.ClustersDeserializer;

@JsonDeserialize (using = ClustersDeserializer.class)
public class Cluster extends HashSet<String> {
  private static final long serialVersionUID = 1L;

  public static class ClustersDeserializer extends JsonDeserializer<Cluster> {

    @Override
    public Cluster deserialize (JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      Cluster cluster = new Cluster ();
      JsonNode n = jp.readValueAsTree ();
      if (n.isArray ())
        for (Iterator<JsonNode> i = ((ArrayNode) n).elements (); i.hasNext ();)
          cluster.add (i.next ().asText ());
      else
        cluster.add (n.asText ());
      return cluster;
    }
  }
}
