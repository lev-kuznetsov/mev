package edu.dfci.cccb.mev.presets.contract;

import static org.jooq.impl.DSL.using;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.jooq.DSLContext;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;
import edu.dfci.cccb.mev.dataset.domain.contract.Analyses;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetBuilderException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDatasetNameException;
import edu.dfci.cccb.mev.dataset.domain.contract.Parser;
import edu.dfci.cccb.mev.dataset.domain.contract.RawInput;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDataset;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDimension;
import edu.dfci.cccb.mev.dataset.domain.tsv.UrlTsvInput;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.dataset.PresetValuesFlatTable;

@Log4j
@Accessors(fluent=false, chain=true)
public class PresetDatasetBuilderFlatTable extends AbstractDatasetBuilder implements PresetDatasetBuilder {

  private @Inject @Named("presets-datasource") DataSource dataSource;
  private DSLContext context;
  
  public PresetDatasetBuilderFlatTable (@Named("presets-datasource") DataSource dataSource) throws SQLException {
    context = using (dataSource.getConnection ());
  }

  @Override
  public Dataset build (PresetDescriptor descriptor, String datasetName, Selection columnSelection) throws PresetException {
    try{
      if(log.isDebugEnabled ())
        log.debug ("Created preset dataset");
      
      RawInput content = new UrlTsvInput (descriptor.dataUrl ());
      Parser parser;
      for (parser = parser (content); parser.next ();) {}
      
      
      Dimension rows = new SimpleDimension (Dimension.Type.ROW, parser.rowKeys (), super.selections (), super.annotation ());
      Dimension columns;
      if(columnSelection!=null)
        columns = new SimpleDimension (Dimension.Type.COLUMN, columnSelection.keys (), super.selections (), super.annotation ());
      else
        columns = new SimpleDimension (Dimension.Type.COLUMN, parser.columnKeys (), super.selections (), super.annotation ());
      
      if(log.isDebugEnabled ())
        log.debug ("columns="+columns);
      if(log.isDebugEnabled ())
        log.debug ("rows="+rows);
      
      Values presetValues = new PresetValuesFlatTable (context, descriptor.name ());
      
      return aggregate (datasetName, presetValues, super.analyses (), columns, rows);
      
    }catch(InvalidDatasetNameException|DatasetBuilderException e){
      throw new PresetException ("Error creating dataset '"+datasetName+"; preset="+descriptor+" columnSelection="+columnSelection, e);
      
    }
    
  }

  @Override
  protected Dataset aggregate (String name, Values values, Analyses analyses, Dimension... dimensions) throws DatasetBuilderException,
                                                                                                      InvalidDatasetNameException {    
    return new SimpleDataset (name, values, analyses, dimensions);
  }

}
