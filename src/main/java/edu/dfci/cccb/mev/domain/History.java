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
package edu.dfci.cccb.mev.domain;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author levk
 * 
 */
@Component
@Scope (value = SCOPE_SESSION, proxyMode = TARGET_CLASS)
@Accessors (fluent = true)
public class History {

  private List<LogEntry> log = new ArrayList<LogEntry> ();

  private @Getter @JsonView List<LogEntry> history = new AbstractList<LogEntry> () {

    @Override
    public LogEntry get (int index) {
      return log.get (index);
    }

    @Override
    public int size () {
      return log.size ();
    }
  };

  public void log (String action) {
    log.add (new LogEntry (new Date (), action));
  }
}
