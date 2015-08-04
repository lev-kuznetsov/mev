package edu.dfci.cccb.mev.configuration.util.prototype;

import java.util.List;

import org.apache.commons.configuration.ConversionException;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils;

import edu.dfci.cccb.mev.configuration.util.contract.Config;

public abstract class AbstractConfig implements Config {

  public AbstractConfig () {
    super ();
  }

  public String getTest () throws Throwable {
    return this.getProperty ("simple.test");
  }

  protected boolean isScalarValue (Object value)
  {
    return ClassUtils.wrapperToPrimitive (value.getClass ()) != null;
  }

  public String[] getStringArray (String key)
  {
    Object value = getProperty (key);
    String[] array = convertToArray (value);
    if (array == null)
      throw new ConversionException ('\'' + key + "' doesn't map to a String/List object");
    return array;
  }

  private String[] convertToArray (Object value) {
    String[] array;

    if (value instanceof String)
    {
      array = new String[1];

      array[0] = (String) value;
    }
    else if (value instanceof List)
    {
      List<?> list = (List<?>) value;
      array = new String[list.size ()];

      for (int i = 0; i < array.length; i++)
      {
        array[i] = ObjectUtils.toString (list.get (i), null);
      }
    }
    else if (value == null)
    {
      array = new String[0];
    }
    else if (isScalarValue (value))
    {
      array = new String[1];
      array[0] = value.toString ();
    }
    else
    {
      return null;
    }
    return array;
  }

  @Override
	public String[] getStringArray(String key, String valueIfNull){
	  String[] value = getStringArray (key);
	  if(value.length==0){
	    value = new String[1];
	    value=convertToArray (valueIfNull);
	  }
	  return value;
	}
}