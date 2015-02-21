/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package edu.dfci.cccb.mev.common.domain2;

import static javax.xml.bind.annotation.XmlAccessType.NONE;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Superclass to all MeV exceptions
 * 
 * @author levk
 * @since ASHA
 */
@XmlAccessorType (NONE)
@XmlRootElement
public abstract class MevException extends Exception {
  private static final long serialVersionUID = 1L;

  public static final String EXCEPTION_TYPE_KEY = "exception.type";
  public static final String EXCEPTION_MESSAGE_KEY = "exception.message";
  public static final String EXCEPTION_CAUSE_KEY = "exception.cause";

  /**
   * Error properties
   */
  private final @XmlElement (name = "exception") Map<String, Object> properties = new HashMap<> ();

  {
    property (EXCEPTION_TYPE_KEY, getClass ().getName ());
  }

  /**
   * 
   */
  public MevException () {}

  /**
   * @param message
   */
  public MevException (String message) {
    super (message);
    property (EXCEPTION_MESSAGE_KEY, message);
  }

  /**
   * @param cause
   */
  public MevException (Throwable cause) {
    super ();
    initCause (cause);
  }

  /**
   * @param message
   * @param cause
   */
  public MevException (String message, Throwable cause) {
    super (message);
    property (EXCEPTION_MESSAGE_KEY, message);
    initCause (cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public MevException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super (message, null, enableSuppression, writableStackTrace);
    property (EXCEPTION_MESSAGE_KEY, message);
    initCause (cause);
  }

  /**
   * Add a property to this exception
   * 
   * @param key
   * @param value
   * @return this exception
   */
  @SuppressWarnings ("unchecked")
  public <E extends MevException, V> E property (String key, V value) {
    properties.put (key, value);
    return (E) this;
  }

  /* (non-Javadoc)
   * @see java.lang.Throwable#initCause(java.lang.Throwable) */
  @Override
  public synchronized Throwable initCause (Throwable cause) {
    property (EXCEPTION_CAUSE_KEY,
              cause instanceof MevException ? cause : (cause.getClass ().getName () + ":" + cause.getMessage ()));
    return super.initCause (cause);
  }

  /* (non-Javadoc)
   * @see java.lang.Throwable#toString() */
  @Override
  public String toString () {
    return super.toString () + properties;
  }
}
