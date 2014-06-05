package edu.dfci.cccb.mev.dataset.domain.export;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import ch.lambdaj.Lambda;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractRawInput;
import edu.dfci.cccb.mev.io.implementation.TemporaryFile;

@Log4j
public class DatasetExportRawInput extends AbstractRawInput {

  private final Dataset dataset;
  private final Selection selection;
  private final Dimension.Type dimension;
  
  public DatasetExportRawInput(String name, Dataset dataset, Selection selection, Dimension.Type dimension){
    name(name);
    this.dataset=dataset;
    this.selection=selection;
    this.dimension=dimension;
  }
  
  
  @Override
  public String contentType () {
    return TAB_SEPARATED_VALUES;
  }

  @Override
  @SneakyThrows({InvalidDimensionTypeException.class, InvalidCoordinateException.class})
  public InputStream input () throws IOException {
    
    List<String> columns = dimension == Type.COLUMN
            ? selection.keys ()
            : dataset.dimension (Type.COLUMN).keys ();
    
    List<String> rows = dimension == Type.ROW 
            ? selection.keys () 
            : dataset.dimension (Type.ROW).keys ();
        
    try (TemporaryFile temp = new TemporaryFile ()) 
    {
      if(log.isDebugEnabled ())
        log.debug ("Created TEMP Export File:"+temp.getAbsolutePath ());
      try (PrintStream out = new PrintStream (temp)) 
      {
          out.println ("\t" + Lambda.join (columns, "\t"));
          for (String row : rows) {
            out.print (row);
            for (String column : columns) {
              out.print ("\t");
              out.print (dataset.values ().get (row, column));
            }
            out.println ();
          }          
      }
      return new FileInputStream (temp);
    }
    
  }

  @Override
  public long size () {
    // TODO Auto-generated method stub
    return 0;
  }

}
