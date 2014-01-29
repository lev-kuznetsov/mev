package edu.dfci.cccb.mev.presets.simple;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eobjects.metamodel.DataContext;
import org.eobjects.metamodel.DataContextFactory;
import org.eobjects.metamodel.convert.Converters;
import org.eobjects.metamodel.convert.StringToIntegerConverter;
import org.eobjects.metamodel.csv.CsvConfiguration;
import org.eobjects.metamodel.csv.CsvDataContext;
import org.eobjects.metamodel.data.DataSet;
import org.eobjects.metamodel.data.Row;
import org.eobjects.metamodel.query.Query;
import org.eobjects.metamodel.schema.Column;
import org.eobjects.metamodel.schema.Schema;
import org.eobjects.metamodel.schema.Table;
import org.eobjects.metamodel.util.UrlResource;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.prototype.AbstractPresetsBuilder;

public class TcgaPresetsBuilder extends AbstractPresetsBuilder  {
    
  @Inject @Named("tcgaPreset") Provider<Preset> tcgaPresetProvider;
  
  @Override
  public Preset createPreset (Object[] values) throws PresetException{
    Preset newPreset = tcgaPresetProvider.get ();
    return newPreset.init(values);
  }
  
}
