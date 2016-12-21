package edu.dfci.cccb.mev.presets.prototype;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.dfci.cccb.mev.presets.dal.TsvReader;
import edu.dfci.cccb.mev.presets.dal.TsvReaderMetaModel;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetsBuilder;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.tcga.ATcgaPresetEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.inject.Inject;
import javax.inject.Named;

@Accessors(fluent = true)
public abstract class AbstractPresetsBuilder implements PresetsBuilder{
    private @Getter @Setter @Inject @Named("tcgaPresetRoot") URL tcgaPresetRoot;
    private @Getter @Setter @Inject @Named ("probe-annotations-root") URL rowAnnotationsRoot;
    private @Getter Class<? extends AbstractTcgaPreset> tcgaPresetClass;
    private @Getter Class<? extends ATcgaPresetEntry> tcgaPresetEntryClass;

    protected Class<? extends ATcgaPresetEntry> getEntryClass(Class<? extends AbstractTcgaPreset> tcgaPresetClass){
        for(Class<?> clazz : tcgaPresetClass.getClasses()){
            if(ATcgaPresetEntry.class.isAssignableFrom(clazz))
                return (Class<? extends ATcgaPresetEntry>)clazz;
        }
        return null;
    }

    public AbstractPresetsBuilder (Class<? extends AbstractTcgaPreset> tcgaPresetClass){
        this.tcgaPresetClass=tcgaPresetClass;
        this.tcgaPresetEntryClass = getEntryClass(tcgaPresetClass);
    }


    @Override
    public Preset createPreset (Object[] values) throws PresetException{
        Preset newPreset = null;
        try {
            newPreset = this.tcgaPresetClass.getDeclaredConstructor(URL.class, URL.class)
                    .newInstance(tcgaPresetRoot(), rowAnnotationsRoot());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new PresetException(e);
        }
        return newPreset.init(values);
    }
//  @Override
//  public List<Preset> getAll(URL data) throws PresetException{
//    CsvDataContext sourceDataContext = new CsvDataContext (data, new CsvConfiguration (1, true, false));                                                     
//    Schema csvSchema = sourceDataContext.getDefaultSchema();
//    
//    Table[] tables = csvSchema.getTables();
//    assert tables.length == 1;
//    Table sourceTable = tables[0];
//    
//    List<Preset> result = new ArrayList<Preset> ();
//    DataSet dataset = sourceDataContext.materializeMainSchemaTable (sourceTable, sourceTable.getColumns (), 0);
//    while(dataset.next ()){
//      Row row = dataset.getRow ();
//      result.add(createPreset (row.getValues ()));
//    }    
//    dataset.close ();
//    return result;
//  }

  
}