package edu.dfci.cccb.mev.configuration.util.archaius;

import java.io.File;
import java.net.URL;
import java.util.Map.Entry;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.netflix.config.AbstractPollingScheduler;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.SimpleDeploymentContext;
import com.netflix.config.sources.S3ConfigurationSource;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.configuration.util.helpers.ConfigurationUtilHelpers;

@Log4j
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
    String s3Bucket = finalConfig.getString ("MEV_CONFIG_BUCKET");
    if(!StringUtils.isEmpty(s3Bucket)){      
      log.info (String.format("*** Processing s3 bucket %s for property file %s", s3Bucket, configName));
      AmazonS3Client s3Client = new AmazonS3Client ();
      s3Client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
      
      addS3Config (finalConfig, s3Client, s3Bucket, profileConfigName);
      addS3Config (finalConfig, s3Client, s3Bucket, configName);      
    }

    // external local folder
    String externalConfigFolder = finalConfig.getString ("MEV_CONFIG_DIR");
    if (!StringUtils.isEmpty (externalConfigFolder)) {
      log.info (String.format("*** Processing external config folder %s for property file %s", externalConfigFolder, configName));
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

  private void addS3Config (ConcurrentCompositeConfiguration finalConfig, AmazonS3Client s3Client, String s3Bucket, String configName) {
  
    try{
      PolledConfigurationSource s3Source = new S3ConfigurationSource(s3Client, s3Bucket, configName);    
      AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler();      
      DynamicConfiguration dynamicConfig = new DynamicConfiguration (s3Source, scheduler);
      //the polling configuration is added at the front to override any other configuration
      finalConfig.addConfigurationAtFront (dynamicConfig, "MEV_BUCKET_CONFIG");
    }catch(AmazonS3Exception e){
      log.warn (String.format("*** Configuration %s was not found in bucket %s; error message: %s", configName, s3Bucket, e.getErrorMessage ()));
    }catch(RuntimeException e){
      log.warn (String.format("*** Configuration %s could not be load from bucket %s due to error: %s", configName, s3Bucket, e.getMessage ()));      
    }
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
    return getProperty ("simple.test");
  }

  @Override
  public String getProperty (String key) {
//    return DynamicPropertyFactory.getInstance ()
//                                 .getStringProperty (name, null).get ();
     return getProperty (key, null);   
  }

  @Override
  public String getProperty (String key, String valueIfNull) {
//    return DynamicPropertyFactory.getInstance ().getStringProperty (key, valueIfNull).getValue ();
    return ((AbstractConfiguration) DynamicPropertyFactory.getInstance ().getBackingConfigurationSource ()).getString (key, valueIfNull);
  }

  @Override
  public String[] getStringArray (String key, String valueIfNull) {
    String[] value =
                     ((AbstractConfiguration) DynamicPropertyFactory.getInstance ().getBackingConfigurationSource ()).getStringArray (key);
    if (value.length==0) {
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
