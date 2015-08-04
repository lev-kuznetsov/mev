package edu.dfci.cccb.mev.configuration.util.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.configuration.util.prototype.AbstractConfig;

public class SimpleConfig extends AbstractConfig implements Config {
  private static final Logger log = Logger.getLogger (SimpleConfig.class);
  private List<Map<String, String>> sources = new LinkedList<Map<String, String>> ();

  private final String fileName;
  private final String fileBaseName;
  private final String fileExt;
  private final String appName;
  private final String envConfigDir;
  
  public SimpleConfig() throws IOException, URISyntaxException{
    this("simple.properties");
  }
  public SimpleConfig(String fileName) throws IOException, URISyntaxException{
    this("MEV", fileName);
  }
  
  public SimpleConfig(String appName, String fileName) throws IOException{
    this.appName = appName;
    this.fileName = fileName;
    this.fileBaseName = FilenameUtils.getBaseName(this.fileName);
    this.fileExt = FilenameUtils.getExtension(this.fileName);   
    this.envConfigDir = this.appName.toUpperCase()+"_CONFIG_DIR";
    
    log.debug (String.format ("Init SimpleConfig fileName: %s", this.fileName));

    URL configUrl;
    Path configPath;

    // load System Properties
    Map<String, String> systemProperties = new HashMap<String, String> (System.getenv ().entrySet ().size ());
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

    // find config directory passed in via System or Environment variable
    String envVarName = this.fileBaseName.toUpperCase()+"_CONFIG_DIR";    
    loadFromEnvFolder(envVarName);    
      
    //check MEV_CONFIG_DIR folder
    loadFromEnvFolder(this.envConfigDir);

    // check current folder
    configPath = Paths.get (fileName);
    configUrl = configPath.toUri ().toURL ();
    log.debug (String.format ("Current folder %s url %s", configPath, configUrl));
    if (Files.exists (configPath)) {
      log.debug (String.format ("Loading URL %s", configUrl));
      sources.add (loadProperties (configUrl.openStream ()));
    }
    // check classpath root
    configUrl = getClass ().getResource ("/" + fileName);
    if (configUrl != null) {
      log.debug (String.format ("Loading URL %s", configUrl));
      sources.add (loadProperties (configUrl.openStream ()));
    }

    // check classpath for the default variables
    configUrl = getClass ().getResource (fileName);
    if (configUrl != null) {
      log.debug (String.format ("Loading URL %s", configUrl));
      sources.add (loadProperties (configUrl.openStream ()));
    }
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.configuration.util.Config#getProperty(java.lang.String) */
  @Override
  public String getProperty (String name) {
    for (Map<String, String> properties : sources) {
      String value = properties.get (name);
      if (value != null)
        return value;
    }
    // throw new Exception(String.format("Property %s not found", name));
    return null;
  }

  private String formatEnvKey (String key) {
    return key.replace ('_', '.');
  }

  private Map<String, String> loadProperties (InputStream is) throws IOException {

    Map<String, String> properties = new HashMap<String, String> ();

    BufferedReader reader = new BufferedReader (new InputStreamReader (is));
    String line;
    while ((line = reader.readLine ()) != null) {
      String[] keyval = line.split ("=");
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
