package edu.dfci.cccb.mev.annotation.domain.probe.h2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.SneakyThrows;

import org.apache.http.MethodNotSupportedException;
import org.h2.tools.Csv;

import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotation;
import edu.dfci.cccb.mev.annotation.domain.probe.contract.ProbeAnnotations;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;

public class H2ProbeAnnotations implements ProbeAnnotations {

  private final String TABLE_NAME_PREFIX="PROBE_ANNOT_";  
  private final String PARAM_TABLE_NAME="[table_name]"; 
  private final String FULL_TABLE_NAME=TABLE_NAME_PREFIX+PARAM_TABLE_NAME;
  private final String SELECT_STATEMENT="select * from table(x VARCHAR=?) t inner join PUBLIC.\""+FULL_TABLE_NAME+"\" mytest on t.x=mytest.PROBESET_ID";
  private final DataSource dataSource;
  private List<String> sources;
  
  @Inject 
  public H2ProbeAnnotations (@Named("probe-annotations-datasource") DataSource dataSource) throws SQLException {
    this.dataSource = dataSource;
    sources = new ArrayList<String> ();
  }

  @Override
  @SneakyThrows
  public List<ProbeAnnotation> get (Dimension dimension) {
    throw new MethodNotSupportedException ("The get(Dimension dimension) method has not been implemented yet");
  }

  @Override
  @SneakyThrows
  public InputStream getAsStream (Dimension dimension, String source) {
    
    InputStream input=null;
    
    try(Connection connection = dataSource.getConnection ()){
      String selectSql = SELECT_STATEMENT.replace (PARAM_TABLE_NAME,  source);
      try(PreparedStatement prep = connection.prepareStatement(selectSql);){        
        
        prep.setObject (1, dimension.keys ().toArray ());        
        try(ResultSet rs = prep.executeQuery();){
          Csv csv = new Csv();
          csv.setFieldSeparatorWrite ("\t");          
          StringWriter writer = new StringWriter();
          csv.write (writer, rs);
          
          byte[] barray = writer.toString().getBytes("UTF-8");
          input = new ByteArrayInputStream(barray);
        }
      }
    }    
    return input;
  }
  

  @Override
  @SneakyThrows
  public List<String> getSources () {
    synchronized (sources) {
      if(sources.size ()==0){
        try(Connection connection= dataSource.getConnection ()){
          try(ResultSet rs = connection.getMetaData ().getTables (null, null, TABLE_NAME_PREFIX+"%", new String[]{"TABLE"}))
          {
            while (rs.next()) {              
              sources.add (rs.getString("TABLE_NAME").replace (TABLE_NAME_PREFIX, ""));
           }
         }
       }      
      }
    }
    return sources;
  }
}
