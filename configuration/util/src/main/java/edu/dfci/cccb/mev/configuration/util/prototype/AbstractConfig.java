package edu.dfci.cccb.mev.configuration.util.prototype;

import javax.inject.Inject;

import org.apache.commons.configuration.ConversionException;
import org.springframework.core.env.Environment;

import edu.dfci.cccb.mev.configuration.util.contract.Config;
import edu.dfci.cccb.mev.configuration.util.helpers.ConfigurationUtilHelpers;

public abstract class AbstractConfig implements Config {

  @Inject protected Environment environment;

  public AbstractConfig () {
    super ();
  }

  public String getTest () throws Throwable {
    return this.getProperty ("simple.test");
  }

  

  public String[] getStringArray (String key)
  {
    Object value = getProperty (key);
    String[] array = ConfigurationUtilHelpers.convertToArray (value);
    if (array == null)
      throw new ConversionException ('\'' + key + "' doesn't map to a String/List object");
    return array;
  }

  

  @Override
	public String[] getStringArray(String key, String valueIfNull){
	  String[] value = getStringArray (key);
	  if(value.length==0){
	    value = new String[1];
	    value= ConfigurationUtilHelpers.convertToArray (valueIfNull);
	  }
	  return value;
	}
  
  @Override
  public String getProperty(String key, String valueIfNull){
    String value = getProperty (key);    
    return value!=null ? value : valueIfNull;
  }

  @Override
  public String[] getProfiles () {
    if(environment!=null)
      return environment.getActiveProfiles ();
    else{
      String sProfiles = getProperty ("spring.profiles.active");
      if(sProfiles!=null){
        String[] result = sProfiles.split (",");
        for(int i=0; i<result.length; i++){
          result[0]=result[0].trim ();
        }
        return result;
      }
      else
        return new String[0];
    }      
  }
}