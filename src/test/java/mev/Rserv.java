package mev;

import org.junit.Ignore;
import org.junit.Test;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.protocol.REXPFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dfci.cccb.mev.dataset.Dataset;
import edu.dfci.cccb.mev.tools.jackson.JaxrsObjectMapperProvider;
import edu.dfci.cccb.mev.tools.jackson.rserve.RserveMapper;

public class Rserv {

  @Ignore
  @Test
  public void r () throws Exception {
    RConnection r = new RConnection ("192.168.99.100", 32303);
    RserveMapper m = new RserveMapper ();
    ObjectMapper o = new JaxrsObjectMapperProvider ().getContext (null);

    Dataset dd =
        o.readerFor (Dataset.class).readValue ("{\"@c\":\"edu.dfci.cccb.mev.dataset.literal.Literal\",\"values\":{"
                                               + "\"r1\":{\"c1\":1,\"c2\":2,\"c3\":3},"
                                               + "\"r2\":{\"c1\":2,\"c2\":3,\"c3\":4},"
                                               + "\"r3\":{\"c1\":3,\"c2\":4,\"c3\":5},"
                                               + "\"r4\":{\"c1\":4,\"c2\":5,\"c3\":6}}}");

    REXPFactory f = new REXPFactory ();
    f.parseREXP (m.writeValueAsBytes (dd), 0);
    r.assign ("dd", f.getREXP ());

    System.out.println (r.eval ("dd").toDebugString ());
  }
}
