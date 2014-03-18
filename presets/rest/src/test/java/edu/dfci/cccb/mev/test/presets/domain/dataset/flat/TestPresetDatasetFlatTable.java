package edu.dfci.cccb.mev.test.presets.domain.dataset.flat;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.Value;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.simple.SimplePresetDescriptor;
import edu.dfci.cccb.mev.presets.util.timer.Timer;
import edu.dfci.cccb.mev.test.presets.configuration.persistence.flat.TestPresetsDatasetFlatTableConfig;
@Log4j
@ContextConfiguration(classes={TestPresetsDatasetFlatTableConfig .class})
@RunWith(SpringJUnit4ClassRunner.class) 
public class TestPresetDatasetFlatTable {

  private @Inject Environment environment; 
  private @Inject @Named("presets-datasource") DataSource dataSource;
  private @Inject PresetDatasetBuilder presetDatasetBuilder;
  private @Inject PresetDimensionBuilder dimensionBuilder;
  @SuppressWarnings("unused") private String rootUrl;
  private boolean bWaitForProfiler=false;
  @PostConstruct
  public void init(){
    this.rootUrl = "file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/";
  }
  
  @Before
  public void load() throws SQLException, PresetException, MalformedURLException{
//    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//    populator.addScript(new ClassPathResource("testPresetDatasetSmall_KIRP.sql"));
//    populator.addScript(new ClassPathResource("testPresetDatasetLargest_GBMLevel2.sql"));    
//    populator.populate (dataSource.getConnection ());
  }  
  
  
  ////////KIRP
  @Test @Ignore
  public void testSmallestTcgaLevel3Iterable () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {
    Dataset presetDataset = getDataset ("KIRP.AgilentG4502A_07_3.Level_3.tsv", "KIRP/Level_3/");
    
    Timer timer = Timer.start ("KIRP-ITERABLE");    
    int count = datasetIterate(presetDataset);
    timer.read ();
    log.debug ("flat-count:"+count);
  }
  @Test @Ignore
  public void testSmallestTcgaLevel3 () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {
    Dataset presetDataset = getDataset ("KIRP.AgilentG4502A_07_3.Level_3.tsv", "KIRP/Level_3/");
    Timer timer = Timer.start ("KIRP-DOUBLE-LOOP");    
    int count = datasetDoublLoop (presetDataset);
    timer.read ();
    log.debug ("flat-count:"+count);
  }
  
  ////////LGG
  @Test @Ignore
  public void testSmallestTcgaLevel2 () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {
    Dataset presetDataset = getDataset ("LGG.AgilentG4502A_07_3.Level_2.tsv", "LGG/Level_2/");
    Timer timer = Timer.start ("LGG-DOUBLE-LOOP");    
    int count = datasetDoublLoop (presetDataset);
    timer.read ();
    log.debug ("flat-count:"+count);
  } 
  @Test @Ignore
  public void testSmallestTcgaLevel2again () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {
    testSmallestTcgaLevel2 ();
  }
  @Test @Ignore
  public void testSmallestTcgaLevel2Iterate () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {
    Dataset presetDataset = getDataset ("LGG.AgilentG4502A_07_3.Level_2.tsv", "LGG/Level_2/");
    Timer timer = Timer.start ("LGG-ITERABLE");    
    int count = datasetIterate (presetDataset);
    timer.read ();
    log.debug ("flat-count:"+count);
  } 
  
  ////////GBM
  @Test @Ignore 
  public void largetsTcgaGBM () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {    
    Dataset presetDataset = getDataset ("GBM.AgilentG4502A_07_2.Level_2.tsv", "GBM/Level_2/");
    
    Timer timer = Timer.start ("LGG-DOUBLE-LOOP");    
    int count=datasetDoublLoop (presetDataset);
    timer.read ();
    log.debug ("flat-count:"+count);

  }
  
  
  
  @Test @Ignore
  public void largetTcgaGBM50ColsIterate() throws PresetException, MalformedURLException, InterruptedException{
    if(bWaitForProfiler){
      log.debug ("Sleeping .. you can start start profiler now.");
      Thread.sleep (1000*15);
    }
    String tsvFileName="GBM.AgilentG4502A_07_2.Level_2.tsv";
    String folder="GBM/Level_2/";
    
    String rootUrl = "file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/";
    PresetDescriptor descriptor = new SimplePresetDescriptor (tsvFileName, 
                                                              new URL (rootUrl), 
                                                              folder+tsvFileName, ""); 
    Dimension columns = dimensionBuilder.buildColumns (descriptor);
    
    List<String> columnList1=new ArrayList<String> (50);
    List<String> columnList2=new ArrayList<String> (50);
    int colsCount=0;
    for(String col : columns.keys ()){
      colsCount++;
      if(colsCount<=50)
        columnList1.add (col);
      else if(colsCount<=100)
        columnList2.add (col);
      else
        break;      
    }
    Selection selection = new SimpleSelection("presest_test", new Properties (), columnList1);
    Dataset presetDataset = presetDatasetBuilder.build (descriptor, "preset_test", selection);
    Timer timer = Timer.start ("GBM-ITERATE-50COLS");    
    int count=datasetIterate (presetDataset);    
    timer.read ();
    log.debug ("flat-count:"+count);
    
    selection = new SimpleSelection("presest_test", new Properties (), columnList2);
    presetDataset = presetDatasetBuilder.build (descriptor, "preset_test", selection);
    timer = Timer.start ("GBM-ITERATE-50COLS-2");    
    count=datasetIterate (presetDataset);    
    timer.read ();
    log.debug ("flat-count2:"+count);
    
    
  }
  
  @Test @Ignore
  public void largetsTcgaGBMIterate () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {    
    Dataset presetDataset = getDataset ("GBM.AgilentG4502A_07_2.Level_2.tsv", "GBM/Level_2/");
    
    Timer timer = Timer.start ("GBM-ITERATE");    
    int count=datasetIterate (presetDataset);
    timer.read ();
    log.debug ("flat-count:"+count);

  }
  
  @SuppressWarnings(value={"unused", "unchecked"})
  private int datasetIterate(Dataset dataset){
    int count=0;
    for(Value oValue : (Iterable<Value>)dataset.values ()){
      double value = oValue.value ();
      count++;
    }
    return count;
  }
  @SuppressWarnings("unused")
  private int datasetDoublLoop(Dataset dataset) throws InvalidDimensionTypeException, InvalidCoordinateException{
    int count=0;
    for (String row : dataset.dimension (Dimension.Type.ROW).keys ()){
      for (String column : dataset.dimension (Dimension.Type.COLUMN).keys ()){
        double value = dataset.values ().get (row, column);
        count++;
      }
    }
    return count;
  }
  private Dataset getDataset(String tsvFileName, String folder) throws InterruptedException, PresetException, MalformedURLException{
    if(bWaitForProfiler){
      log.debug ("Sleeping .. you can start start profiler now.");
      Thread.sleep (1000*15);
    }    
    ;
    String rootUrl = "file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/";
    PresetDescriptor descriptor = new SimplePresetDescriptor (tsvFileName, 
                                                              new URL (rootUrl), 
                                                              folder+tsvFileName, ""); 
    return presetDatasetBuilder.build (descriptor, "preset_test", null);
  }
  
}
