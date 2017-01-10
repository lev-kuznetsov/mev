package edu.dfci.cccb.mev.workspace;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.Dataset;

public class ProjectTest {

  private ObjectMapper m = new ObjectMapper ();

  @Ignore
  @Test
  public void write () throws Exception {
    Dataset d = new Dataset ();
    d.bind (m.readerFor (new TypeReference <Map <String, Map <String, Double>>> () {}).readValue ("{\"r1\":{\"c1\":0.1,\"c2\":0.2},\"r2\":{\"c1\":0.2,\"c2\":0.3},\"r3\":{\"c1\":0.3,\"c2\":0.4},\"r4\":{\"c1\":0.4,\"c2\":0.5}}"));
    System.out.println (m.writeValueAsString (d));
    System.out.println (d.values ().query (m.readerFor (new TypeReference <Map <String, List <String>>> () {}).readValue ("{\"row\":[\"r1\",\"r3\",\"r4\"],\"column\":[\"c1\",\"c2\"]}")));
    System.out.println (m.readValue ("{\"@c\":\".Dataset\",\"values\":{\"r1\":{\"c1\":0.1,\"c2\":0.2},\"r2\":{\"c1\":0.2,\"c2\":0.3},\"r3\":{\"c1\":0.3,\"c2\":0.4},\"r4\":{\"c1\":0.4,\"c2\":0.5}}}",
                                     Dataset.class));
  }
}
