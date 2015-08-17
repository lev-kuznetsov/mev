package edu.dfci.cccb.mev.configuration.util.helpers;

import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils;

public class ConfigurationUtilHelpers {

  public static boolean isScalarValue (Object value)
  {
    return ClassUtils.wrapperToPrimitive (value.getClass ()) != null;
  }
  
  public static String[] convertToArray (Object value) {
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
}
