package edu.dfci.cccb.mev.dataset.domain.r;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.jpmml.rexp.RExp;
import org.jpmml.rexp.RString;

import com.google.protobuf.CodedOutputStream;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.MevException;

@Log4j
public class ProtobufSerializer {

  public ProtobufSerializer () {}

  public void serialize (Dataset dataset, OutputStream target) throws InvalidDimensionTypeException,
                                                              InvalidCoordinateException,
                                                              IOException,
                                                              MevException {

    RExp.Builder builder = RExp.newBuilder ().setRclass (RExp.RClass.LIST);

    for (String column : dataset.dimension (Type.COLUMN).keys ()) {
      RExp.Builder valuesBuilder = RExp.newBuilder ().setRclass (RExp.RClass.REAL);
      for (String row : dataset.dimension (Type.ROW).keys ()) {
        valuesBuilder.addRealValue (dataset.values ().get (row, column));
      }
      builder.addRexpValue (valuesBuilder);
    }

    // FieldDescriptor fieldAttrName = RExp.getDescriptor ().findFieldByNumber
    // (RExp.ATTRNAME_FIELD_NUMBER);
    builder.addAttrName ("names");
    RExp.Builder namesAttrBuilder = RExp.newBuilder ().setRclass (RExp.RClass.STRING);
    for (String column : dataset.dimension (Type.COLUMN).keys ()) {
      namesAttrBuilder.addStringValue (RString.newBuilder ().setStrval (column).setIsNA (false));
    }
    builder.addAttrValue (namesAttrBuilder);

    builder.addAttrName ("class");
    builder.addAttrValue (
           RExp.newBuilder ().setRclass (RExp.RClass.STRING).addStringValue (
                                                                             RString.newBuilder ()
                                                                                    .setStrval ("data.frame")
                                                                                    .setIsNA (false)
               )
           );

    builder.addAttrName ("row.names");
    RExp.Builder rowNamesAttrBuilder = RExp.newBuilder ().setRclass (RExp.RClass.STRING);
    for (String row : dataset.dimension (Type.ROW).keys ()) {
      rowNamesAttrBuilder.addStringValue (RString.newBuilder ().setStrval (row).setIsNA (false));
    }
    builder.addAttrValue (rowNamesAttrBuilder);

    CodedOutputStream cos = CodedOutputStream.newInstance (target);
    RExp result = builder.build ();
    // assertTrue(result.isInitialized ());
    List<String> errors = result.findInitializationErrors ();
    if (errors.size () > 0)
      throw new MevException (String.format ("Serialization errors: %s", errors)) {
        private static final long serialVersionUID = 1L;
      };
    result.writeTo (cos);
    cos.flush ();

  }

}
