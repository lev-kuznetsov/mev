package edu.dfci.cccb.mev.annotation.domain.probe.metafile;

import static org.eobjects.metamodel.csv.CsvConfiguration.DEFAULT_COLUMN_NAME_LINE;
import static org.eobjects.metamodel.csv.CsvConfiguration.DEFAULT_ESCAPE_CHAR;
import static org.eobjects.metamodel.csv.CsvConfiguration.DEFAULT_QUOTE_CHAR;
import static org.eobjects.metamodel.util.FileHelper.DEFAULT_ENCODING;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eobjects.metamodel.csv.CsvConfiguration;
import org.eobjects.metamodel.csv.CsvDataContext;
import org.eobjects.metamodel.data.DataSet;
import org.eobjects.metamodel.schema.Schema;
import org.eobjects.metamodel.schema.Table;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotationSource;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.exceptions.ProbeAnnotationSourceNotFoundException;
import edu.dfci.cccb.mev.annotation.domain.probe.prototype.AbstractProbeAnnotatinsSources;

public class MetafileProbeAnnotationSources extends AbstractProbeAnnotatinsSources {

  private final List<ProbeAnnotationSource> sources;
  
  
  
  public MetafileProbeAnnotationSources (URL root, URL metadata) throws MalformedURLException {

    sources = new ArrayList<ProbeAnnotationSource>();
    
    CsvConfiguration config = new CsvConfiguration(
      DEFAULT_COLUMN_NAME_LINE, DEFAULT_ENCODING, '\t', DEFAULT_QUOTE_CHAR, DEFAULT_ESCAPE_CHAR, true, false);
    
    CsvDataContext sourceDataContext;
    Table sourceTable;
    sourceDataContext = new CsvDataContext (metadata, config);                                                     
    Schema csvSchema = sourceDataContext.getDefaultSchema();
    
    Table[] tables = csvSchema.getTables();
    assert tables.length == 1;
    sourceTable = tables[0];    

    
    try(DataSet dataset = sourceDataContext.materializeMainSchemaTable (sourceTable, sourceTable.getColumns (), 0)){
      while(dataset.next ()){
        sources.add (new MetafileProbeAnnotationSource (root, dataset.getRow ().getValues ()));
      }
    }
            
  }

  @Override
  public List<ProbeAnnotationSource> getAll () {
    return sources;
  }

  @Override
  public ProbeAnnotationSource get (String name) throws ProbeAnnotationSourceNotFoundException {
    for(ProbeAnnotationSource source : sources)
      return source;
    throw new ProbeAnnotationSourceNotFoundException("Cant fine name: '"+name+"'");
  }

}
