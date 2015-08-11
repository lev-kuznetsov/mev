package edu.dfci.cccb.mev.configuration.util.archaius;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;

import lombok.SneakyThrows;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.SimpleDeploymentContext;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.configuration.util.helpers.ConfigurationUtilHelpers;

public class ArchaiusConfig implements Config {

  private String getProfile(AbstractConfiguration finalConfig){
    String profile=null;
    profile = ConfigurationManager.getDeploymentContext ().getDeploymentEnvironment ();
    if(profile==null){
      profile = finalConfig.getString ("spring.profiles.active");
    }
    return profile;
  }
  
  @SneakyThrows (value = { ConfigurationException.class })
  public ArchaiusConfig (String configName) {
    // ConfigurationManager.loadCascadedPropertiesFromResources(configName);
    
    // add them in this order to make dynamicConfig override myConfiguration
    ConcurrentCompositeConfiguration finalConfig;
    if (!ConfigurationManager.isConfigurationInstalled ()) {
      finalConfig = new ConcurrentCompositeConfiguration ();
            
      // system
      SystemConfiguration systemConfig = new SystemConfiguration ();
      finalConfig.addConfiguration (systemConfig);

      // env
      EnvironmentConfiguration envConfig = new EnvironmentConfiguration ();
      AbstractConfiguration envConfig2 = new PropertiesConfiguration ();
      for (Entry<String, Object> entry : envConfig.getMap ().entrySet ()) {
        envConfig2.addProperty (entry.getKey (), entry.getValue ());
        if (entry.getKey ().contains ("_"))
          envConfig2.addProperty (entry.getKey ().replace ("_", "."), entry.getValue ());
      }
      finalConfig.addConfiguration (envConfig2);
            
      String profile = finalConfig.getString ("spring.profiles.active");
      if(profile==null)
        profile = ConfigurationManager.getDeploymentContext ().getDeploymentEnvironment ();
      if(profile!=null){
        DeploymentContext deploymentContext = new SimpleDeploymentContext ();
        deploymentContext.setDeploymentEnvironment (profile);
        ConfigurationManager.setDeploymentContext (deploymentContext);
      }      
    }
    else {
      finalConfig = (ConcurrentCompositeConfiguration) ConfigurationManager.getConfigInstance ();
    }

    String baseFileName = FilenameUtils.getFullPath (configName) + FilenameUtils.getBaseName (configName);
    String fileExt = FilenameUtils.getExtension(configName);   
    String profileConfigName = baseFileName + "-" + ConfigurationManager.getDeploymentContext ().getDeploymentEnvironment () + "." + fileExt;

    // external polled folder
    DynamicConfiguration dynamicConfig = new DynamicConfiguration ();
    finalConfig.addConfiguration (dynamicConfig);

    // external local folder
    String externalConfigFolder = finalConfig.getString ("MEV_CONFIG_DIR");
    if (externalConfigFolder != null) {      
      addExternalConfig (finalConfig, profileConfigName, externalConfigFolder);      
      addExternalConfig (finalConfig, configName, externalConfigFolder);
    }

    // current dir    
    addExternalConfig(finalConfig, profileConfigName, null);
    addExternalConfig(finalConfig, configName, null);
    
    // packaged in jar
    addClasspathConfig (profileConfigName, finalConfig);
    addClasspathConfig (configName, finalConfig);

    if (!ConfigurationManager.isConfigurationInstalled ())
      ConfigurationManager.install (finalConfig);
  }

  private void addClasspathConfig (String configName, ConcurrentCompositeConfiguration finalConfig) throws ConfigurationException {
    URL url = getClass().getResource (configName);
    if(url==null)
      url = getClass ().getClassLoader ().getResource (configName);
    
    if(url != null){      
      PropertiesConfiguration jarPropConfig = new PropertiesConfiguration (url);
      finalConfig.addConfiguration (jarPropConfig);
    }
  }

  private void addExternalConfig (ConcurrentCompositeConfiguration finalConfig, String configName, String configFolder) throws ConfigurationException {
    if(configFolder != null)      
      configFolder += File.separator + FilenameUtils.getFullPath (configName);
    else
      configFolder = FilenameUtils.getFullPath (configName);
    
    configName = FilenameUtils.getName (configName);
    URL url =  ConfigurationUtils.locate (configFolder, configName);
    if(url!=null){
      PropertiesConfiguration externalFolderPropConfig =
            new PropertiesConfiguration (url);
      finalConfig.addConfiguration (externalFolderPropConfig);
    }
  }

  @Override
  public String getTest () throws Throwable {
    return null;
  }

  @Override
  public String getProperty (String name) {
    return DynamicPropertyFactory.getInstance ()
                                 .getStringProperty (name, null).get ();
  }

  @Override
  public String getProperty (String name, String valueIfNull) {
    return DynamicPropertyFactory.getInstance ().getStringProperty (name, valueIfNull).get ();
  }

  @Override
  public String[] getStringArray (String key, String valueIfNull) {
    String[] value =
                     ((AbstractConfiguration) DynamicPropertyFactory.getInstance ().getBackingConfigurationSource ()).getStringArray (key);
    if (value == null) {
      value = ConfigurationUtilHelpers.convertToArray (valueIfNull);
    }
    return value;
  }

  @Override
  public String[] getStringArray (String key) {
    return ((AbstractConfiguration) DynamicPropertyFactory.getInstance ().getBackingConfigurationSource ()).getStringArray (key);
  }

  @Override
  public String[] getProfiles () {
    String[] value = { ConfigurationManager.getDeploymentContext ().getDeploymentEnvironment () };
    return value;
  }

}
