/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.controllers;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import us.levk.spring.web.annotation.Cookies;
import edu.dfci.cccb.mev.domain.Preferences;

/**
 * @author levk
 * 
 */
@Controller
@RequestMapping ("/preferences")
@Accessors (fluent = true, chain = true)
@Log4j
public class PreferencesController {

  private @Autowired Preferences configuration;

  private @Getter @Setter String comment = "MeV preference";
  private @Getter @Setter String domain = null;
  private @Getter @Setter boolean httpOnly = true;
  private int maxAge = (int) SECONDS.convert (30, DAYS);
  private @Getter @Setter String path = "/preferences";
  private @Getter @Setter boolean secure = true;

  public long maxAge (TimeUnit unit) {
    return unit.convert (maxAge, SECONDS);
  }

  public PreferencesController maxAge (long duration, TimeUnit unit) {
    maxAge = (int) SECONDS.convert (duration, unit);
    return this;
  }

  @RequestMapping (value = "/{key:.+}", method = GET)
  @ResponseBody
  public String get (@PathVariable ("key") String key,
                     @Cookies Collection<Cookie> cookies) throws PreferenceKeyNotFoundException {
    String result = configuration.get (key, getClientPreferences (cookies));
    if (result == null)
      throw new PreferenceKeyNotFoundException (key);
    else
      return result;
  }

  @RequestMapping (value = "/{key:.+}", method = PUT)
  @ResponseStatus (OK)
  public void put (@PathVariable ("key") String key,
                   @RequestParam ("value") String value,
                   @Cookies Collection<Cookie> cookies) {
    configuration.put (key, value, getClientPreferences (cookies));
  }

  @ExceptionHandler (PreferenceKeyNotFoundException.class)
  @ResponseStatus (NOT_FOUND)
  @ResponseBody
  public String handleNotFound (PreferenceKeyNotFoundException e) {
    return e.getLocalizedMessage ();
  }

  private Properties getClientPreferences (final Collection<Cookie> cookies) {
    return new Properties () {
      private static final long serialVersionUID = 1L;
      
      {
        for (Cookie cookie : cookies)
          super.put (cookie.getName (), cookie.getValue ());
      }
      
      /* (non-Javadoc)
       * @see java.util.Hashtable#get(java.lang.Object)
       */
      @Override
      @Synchronized
      public Object get (Object key) {
        Object result = super.get (key);
        if (log.isDebugEnabled () && result != null)
          log.debug ("Found key \'" + key + "\' in client preferences");
        return result;
      }

      /* (non-Javadoc)
       * @see java.util.Hashtable#remove(java.lang.Object) */
      @Override
      @Synchronized
      public Object remove (Object key) {
        for (Iterator<Cookie> iterator = cookies.iterator (); iterator.hasNext ();)
          if (key.equals (iterator.next ().getName ()))
            iterator.remove ();
        return super.remove (key);
      }

      /* (non-Javadoc)
       * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object) */
      @Override
      @Synchronized
      public Object put (Object key, Object value) {
        cookies.add (new Cookie ((String) key, (String) value) {
          private static final long serialVersionUID = 1L;

          {
            if (comment != null)
              setComment (comment);
            if (domain != null)
              setDomain (domain ());
            setHttpOnly (httpOnly);
            setMaxAge (maxAge);
            if (path != null)
              setPath (path);
            setSecure (secure);
          }
        });
        return super.put (key, value);
      }
    };
  }
}
