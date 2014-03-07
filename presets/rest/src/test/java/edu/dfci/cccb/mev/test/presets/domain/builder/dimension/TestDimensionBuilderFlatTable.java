package edu.dfci.cccb.mev.test.presets.domain.builder.dimension;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.DimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.presets.dataset.flat.PresetDimensionBuilderFlatTable;
import edu.dfci.cccb.mev.presets.simple.SimplePresetDescriptor;
import edu.dfci.cccb.mev.presets.util.timer.Timer;
import edu.dfci.cccb.mev.test.presets.configuration.persistence.flat.TestJooqCursorGBMLevel2Configuration;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestJooqCursorGBMLevel2Configuration.class})
public class TestDimensionBuilderFlatTable {

  private @Inject Environment environment;
  private @Inject PresetDimensionBuilder dimensionBuilder;
  private @Inject @Named("presets-jooq-context") DSLContext context;
  
  @PostConstruct
  public void init(){
//     this.dimensionBuilder=new PresetDimensionBuilderFlatTable (context);
  }
  @Before
  public void setup(){
//    this.dimensionBuilder=new PresetDimensionBuilderFlatTable (context);
  }
  
  @Test @Ignore
  public void testBuildRows() throws PresetException, MalformedURLException, InterruptedException{
    Thread.sleep (1000*100);
    String tsvFileName="GBM.AgilentG4502A_07_2.Level_2.tsv";
    String folder="GBM/Level_2/";
    
    String rootUrl = "file://"+environment.getProperty ("user.home")+"/mev/data/tcga/tcga_data/";
    PresetDescriptor descriptor = new SimplePresetDescriptor (tsvFileName, 
                                                              new URL (rootUrl), 
                                                              folder+tsvFileName, ""); 
    Timer timer = Timer.start ("GBM-DIMENSION-BUILDER");
    Dimension rows = dimensionBuilder.buildRows (descriptor);
    timer.read ();
  }
  
  @Test
  public void testBuildRows2() throws PresetException, MalformedURLException, InterruptedException{
    testBuildRows ();
  }
  
  @Test
  public void testBuildRows3() throws PresetException, MalformedURLException, InterruptedException{
    testBuildRows ();
  }
  
  @Test
  public void testBuildRows4() throws PresetException, MalformedURLException, InterruptedException{
    testBuildRows ();
  }
}
