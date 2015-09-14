package edu.dfci.cccb.mev.test.presets.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetDomainBuildersConfiguration;
import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDimensionBuilder;
import edu.dfci.cccb.mev.presets.contract.Presets;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DatasetDomainBuildersConfiguration.class, PresetsRestToolConfiguration.class, ProbeAnnotationsPersistanceConfigTool.class})
public class PresetsFindMinMax {

  private @Inject Presets presets;
  private @Inject @Named("presets-datasource") DataSource dataSource;
  private @Inject PresetDimensionBuilder dimensionBuilder;
    
  @ToString
  @AllArgsConstructor
  private static class SampleStats{
    @Getter @Setter private String tableName;
    @Getter @Setter private String sampelName;
    @Getter @Setter private double  min;
    @Getter @Setter private double max;
        
  }
  
  private SampleStats getMinMax(String tableName, String column) throws SQLException{
    String query ="SELECT '[TABLE]', min(cast(\"[COLUMN]\" as double)) as \"min\", max(cast(\"[COLUMN]\" as double)) as \"max\" FROM \"PUBLIC\".\"[TABLE]\" WHERE NOT REGEXP_MATCHES(LCASE(\"[COLUMN]\"),  '.*[a-z].*')";
    query = query.replace ("[COLUMN]", column).replace ("[TABLE]", tableName);
    try(Connection connection = dataSource.getConnection ()){
      try(Statement statement = connection.createStatement ()){
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
          
          double min = rs.getDouble ("min");
          double max = rs.getDouble ("max");          
          return new SampleStats (tableName, column, min, max);
        }
        throw new SQLException ("something is wrong: "+tableName+"."+column);
      }
    }
  }
  
  
  @Test 
  public void test () throws SQLException, PresetException {
    
    for(Preset preset : presets.getAll ()){
      Dimension columns = dimensionBuilder.buildColumns (preset.descriptor ());
//      System.out.println(preset.name () + "\t"+columns.keys ());
      SampleStats tableMinMax = new SampleStats (preset.name (), "all", Double.MAX_VALUE, Double.MIN_VALUE);
      int maxSamples = 10;
      int sampleCount=0;
      for(String column : columns.keys ()){
        SampleStats sampleMinMax = getMinMax("PRESET-"+preset.name (), column);
        if(sampleMinMax.getMin()<tableMinMax.getMin ()){
          tableMinMax.setMin (sampleMinMax.getMin());
        }
        if(sampleMinMax.getMax()>tableMinMax.getMax ()){
          tableMinMax.setMax (sampleMinMax.getMax());
        }        
        if(tableMinMax.getMax ()-tableMinMax.getMin() > 1000)
          break;      
        if(sampleCount>=maxSamples)
          break;
        sampleCount++;        
      }
      System.out.println(tableMinMax);              
      
    }
  }

}
