package edu.dfci.cccb.mev.annotation.domain.probe.metafile;

import static org.eobjects.metamodel.csv.CsvConfiguration.DEFAULT_COLUMN_NAME_LINE;
import static org.eobjects.metamodel.csv.CsvConfiguration.DEFAULT_ESCAPE_CHAR;
import static org.eobjects.metamodel.csv.CsvConfiguration.DEFAULT_QUOTE_CHAR;
import static org.eobjects.metamodel.util.FileHelper.DEFAULT_ENCODING;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.eobjects.metamodel.csv.CsvConfiguration;
import org.eobjects.metamodel.csv.CsvDataContext;
import org.eobjects.metamodel.data.DataSet;
import org.eobjects.metamodel.schema.Schema;
import org.eobjects.metamodel.schema.Table;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationPlatform;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationPlatformFactory;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.ProbeAnnotationSourceNotFoundException;
import edu.dfci.cccb.mev.annotation.domain.probe.prototype.AbstractProbeAnnotationPlatforms;

public class MetafileProbeAnnotationPlatforms extends AbstractProbeAnnotationPlatforms {

  private final List<ProbeAnnotationPlatform> platforms;    
  @Getter @Setter @Inject ProbeAnnotationPlatformFactory platformFactory;
  
  @Inject
  public MetafileProbeAnnotationPlatforms (ProbeAnnotationPlatformFactory platformFactory) throws MalformedURLException {
    
    this.platformFactory=platformFactory;
    platforms = new ArrayList<ProbeAnnotationPlatform>();
                
  }

  public void loadFromFile(URL metadata){
    CsvConfiguration config = new CsvConfiguration(DEFAULT_COLUMN_NAME_LINE, DEFAULT_ENCODING, '\t', DEFAULT_QUOTE_CHAR, DEFAULT_ESCAPE_CHAR, true, false);
     
     CsvDataContext sourceDataContext;
     Table sourceTable;
     sourceDataContext = new CsvDataContext (metadata, config);                                                     
     Schema csvSchema = sourceDataContext.getDefaultSchema();
     
     Table[] tables = csvSchema.getTables();
     assert tables.length == 1;
     sourceTable = tables[0];    
    
     
     try(DataSet dataset = sourceDataContext.materializeMainSchemaTable (sourceTable, sourceTable.getColumns (), 0)){
       while(dataset.next ()){
         platforms.add (platformFactory.create (dataset.getRow ().getValues ()));
       }
     }

  }
  
  @Override
  public List<ProbeAnnotationPlatform> getAll () {
    return platforms;
  }

  @Override
  public ProbeAnnotationPlatform get (String name) throws ProbeAnnotationSourceNotFoundException {
    for(ProbeAnnotationPlatform source : platforms){
      if(source.name ().equalsIgnoreCase (name))
        return source;
    }
    throw new ProbeAnnotationSourceNotFoundException("Cant fine name: '"+name+"'");
  }

}
