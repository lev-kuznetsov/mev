package edu.dfci.cccb.mev.configuration.util.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.apache.commons.io.FilenameUtils;
import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.configuration.util.prototype.AbstractConfig;

@Log4j
public class SimpleConfig extends AbstractConfig implements Config {
//  private static final Logger log = Logger.getLogger (SimpleConfig.class);
  private List<Map<String, String>> sources = new LinkedList<Map<String, String>> ();

  private final String fileName;  
  private final String baseFileName;
  private final String fileExt;
  private final String appName;
  private final String envConfigDir;
  private String envHomeDir;  
  
  public SimpleConfig() throws IOException, URISyntaxException{
    this("simple.properties");
  }
   
  public SimpleConfig(String fileName){
    this("mev", fileName);
  }
  
  @SneakyThrows(value={IOException.class})
  public SimpleConfig(String appName, String fileName) {
    this.appName = appName;
    this.fileName = fileName;
    this.baseFileName = FilenameUtils.getFullPath (fileName) + FilenameUtils.getBaseName (fileName);
    this.fileExt = FilenameUtils.getExtension(this.fileName);   
    this.envConfigDir = this.appName.toUpperCase()+"_CONFIG_DIR";
    this.envHomeDir = this.appName.toUpperCase ()+"_HOME";    
    
    log.debug (String.format ("Init SimpleConfig fileName: %s; %s", this.fileName, this.baseFileName));

    URL configUrl;
    Path configPath;
        
    // load System Properties
    Map<String, String> systemProperties = new HashMap<String, String> (System.getProperties ().entrySet ().size ());
    for (Entry<Object, Object> entry : System.getProperties ().entrySet ()) {
      systemProperties.put (formatEnvKey ((String) entry.getKey ()), (String) entry.getValue ());
    }
    sources.add (systemProperties);

    // load Env Properties
    Map<String, String> envProperties = new HashMap<String, String> (System.getenv ().entrySet ().size ());
    for (Entry<String, String> entry : System.getenv ().entrySet ()) {
      envProperties.put (formatEnvKey (entry.getKey ()), entry.getValue ());
    }    
    sources.add (envProperties);    
    
    // check for home directory
    String homeDir = getProperty (this.envHomeDir);
    if(homeDir == null){
      homeDir = System.getProperty ("user.home")+System.getProperty ("file.separator")+this.appName;
      System.setProperty (this.envHomeDir, homeDir);
      systemProperties.put (this.envHomeDir, homeDir);
    }
    log.debug(String.format("MEV_HOME is %s", homeDir));
    
    //check MEV_CONFIG_DIR folder
    loadFromEnvFolder(this.envConfigDir);

    // check current folder
    for(String curFileName : getFileNameToCheck()){     
      configPath = Paths.get (curFileName);
      configUrl = configPath.toUri ().toURL ();
      log.debug (String.format ("Current folder %s url %s", configPath, configUrl));
      if (Files.exists (configPath)) {
        log.debug (String.format ("Loading URL %s", configUrl));
        sources.add (loadProperties (configUrl.openStream ()));
      }
    }
    
    // check classpath root
    for(String curFileName : getFileNameToCheck()){
      configUrl = getClass ().getResource ("/" + curFileName );
      if (configUrl != null) {
        log.debug (String.format ("Loading URL %s", configUrl));
        sources.add (loadProperties (configUrl.openStream ()));
      }
    }

    // check classpath for the default variables
    for(String curFileName : getFileNameToCheck()){    
      configUrl = getClass ().getResource (curFileName);
      if (configUrl != null) {
        log.debug (String.format ("Loading URL %s", configUrl));
        sources.add (loadProperties (configUrl.openStream ()));
      }
    }
  }  
  
  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.util.Config#getProperty(java.lang.String) */
  @Override
  public String getProperty (String name) {
    name = formatEnvKey (name);
    String value = null;
    for (Map<String, String> properties : sources) {
      value = properties.get (name);
      if (value != null) break;
      value = properties.get (name.toLowerCase ());
      if (value != null) break;
      value = properties.get (name.toUpperCase ());
      if (value != null) break;
    }
    if(value == null && this.environment!=null){
      value = environment.getProperty (name); 
    }
    if(value!=null && value.contains ("${")){
      if(this.environment!=null)
        value = this.environment.resolvePlaceholders (value);
    }
    // throw new Exception(String.format("Property %s not found", name));
    return value;
  }
  private Iterable<String> getFileNameToCheck(){
    List<String> result = new ArrayList<String> ();
    for(String profile : getProfiles ())
      result.add (this.baseFileName + "-" + profile + "." + this.fileExt);
    result.add (this.fileName);
    return result;
  }
  
  private String formatEnvKey (String key) {
    return key.replace ('_', '.');
  }

  private Map<String, String> loadProperties (InputStream is) throws IOException {

    Map<String, String> properties = new HashMap<String, String> ();

    BufferedReader reader = new BufferedReader (new InputStreamReader (is));
    String line;
    while ((line = reader.readLine ()) != null) {
      String[] keyval = line.split ("=", 2);
      if (keyval.length > 1) {
        log.debug (String.format ("Adding property %s value %s", keyval[0], keyval[1]));
        properties.put (keyval[0].trim (), keyval[1].trim ());
      } else if (keyval.length > 0) {
        log.debug (String.format ("Adding blank property %s", keyval[0]));
        properties.put (keyval[0].trim (), "");
      } else {
        log.debug (String.format ("Skipping line %s", line));
      }
    }
    return properties;
  }

  private Map<String, String> loadFromEnvFolder (String envVarName) throws MalformedURLException, IOException {
    URL configUrl;
    Path configPath;
    String configDir = getProperty (formatEnvKey (envVarName));
    if (configDir != null) {
      configPath = Paths.get (configDir + "/" + this.fileName);
      configUrl = configPath.toUri ().toURL ();
      log.debug (String.format ("%s for file %s url %s", envVarName, configDir, configUrl));
      if (Files.exists (configPath)) {
        log.debug (String.format ("Loading URL %s", configUrl));
        sources.add (loadProperties (configUrl.openStream ()));
      } else {
        log.debug (String.format ("ConfigHome path does not exist: %s", configPath));
      }
    } else {
      log.debug (String.format ("%s is null", envVarName));
    }
    return null;
  }
}
