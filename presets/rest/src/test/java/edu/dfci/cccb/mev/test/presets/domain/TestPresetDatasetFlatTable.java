package edu.dfci.cccb.mev.test.presets.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.presets.contract.PresetDatasetBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetValuesLoader;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.simple.SimplePresetDescriptor;
@Log4j
@ContextConfiguration(classes={TestPresetsDatasetFlatTableConfig .class})
@RunWith(SpringJUnit4ClassRunner.class) 
public class TestPresetDatasetFlatTable {

  private @Inject Environment environment; 
  private @Inject @Named("presets-datasource") DataSource dataSource;
  private @Inject PresetDatasetBuilder presetDatasetBuilder;
  private @Inject PresetValuesLoader loader;
  private String rootUrl;
  
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
  
  @Test @Ignore
  public void testSmallestTcgaLevel3 () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {
    log.debug ("Sleeping .. you can start start profiler now.");    
    Thread.sleep (1000*15);
    String tsvFileName="KIRP.AgilentG4502A_07_3.Level_3.tsv";
    String rootUrl = "file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/";
    PresetDescriptor descriptor = new SimplePresetDescriptor (tsvFileName, 
                                                              new URL (rootUrl), 
                                                              "KIRP/Level_3/"+tsvFileName, ""); 
    Dataset presetDataset = presetDatasetBuilder.build (descriptor, "preset_test", null);
 
    long startTime = System.nanoTime();    
    int count=0;
    for (String row : presetDataset.dimension (Dimension.Type.ROW).keys ()){
      for (String column : presetDataset.dimension (Dimension.Type.COLUMN).keys ()){
        double value = presetDataset.values ().get (row, column);
        count++;
      }
    }
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    log.debug ("flat-count:"+count+", duration: "+duration);
  }
 
  @Test 
  public void testSmallestTcgaLevel2 () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {
//    log.debug ("Sleeping .. you can start start profiler now.");    
//    Thread.sleep (1000*15);
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";
    String rootUrl = "file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/";
    PresetDescriptor descriptor = new SimplePresetDescriptor (tsvFileName, 
                                                              new URL (rootUrl), 
                                                              "LGG/Level_2/"+tsvFileName, ""); 
    Dataset presetDataset = presetDatasetBuilder.build (descriptor, "preset_test", null);
 
    long startTime = System.nanoTime();    
    int count=0;
    for (String row : presetDataset.dimension (Dimension.Type.ROW).keys ()){
      for (String column : presetDataset.dimension (Dimension.Type.COLUMN).keys ()){
        double value = presetDataset.values ().get (row, column);
        count++;
      }
    }
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    log.debug ("flat-count:"+count+", duration: "+duration);
  }
 
  @Test 
  public void testSmallestTcgaLevel22 () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {
//    log.debug ("Sleeping .. you can start start profiler now.");    
//    Thread.sleep (1000*15);
    String tsvFileName="LGG.AgilentG4502A_07_3.Level_2.tsv";
    String rootUrl = "file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/";
    PresetDescriptor descriptor = new SimplePresetDescriptor (tsvFileName, 
                                                              new URL (rootUrl), 
                                                              "LGG/Level_2/"+tsvFileName, ""); 
    Dataset presetDataset = presetDatasetBuilder.build (descriptor, "preset_test", null);
 
    long startTime = System.nanoTime();    
    int count=0;
    for (String row : presetDataset.dimension (Dimension.Type.ROW).keys ()){
      for (String column : presetDataset.dimension (Dimension.Type.COLUMN).keys ()){
        double value = presetDataset.values ().get (row, column);
        count++;
      }
    }
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    log.debug ("flat-count:"+count+", duration: "+duration);
  }
  
  @Test @Ignore 
  public void largetsSmallestTcga () throws PresetException, MalformedURLException, InvalidDimensionTypeException, InvalidCoordinateException, SQLException, InterruptedException {
    Thread.sleep (1000*10); 
    String tsvFileName="GBM.AgilentG4502A_07_2.Level_2.tsv";
    String rootUrl = "file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/";
    PresetDescriptor descriptor = new SimplePresetDescriptor (tsvFileName, 
                                                              new URL (rootUrl), 
                                                              "GBM/Level_2/"+tsvFileName, ""); 
    Dataset presetDataset = presetDatasetBuilder.build (descriptor, "preset_test", null);
 
    for (String row : presetDataset.dimension (Dimension.Type.ROW).keys ()){
      for (String column : presetDataset.dimension (Dimension.Type.COLUMN).keys ()){
        double value = presetDataset.values ().get (row, column);
      }
    }
  }
}
