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

package edu.dfci.cccb.mev.common.domain.guice.c3p0;

import java.beans.PropertyVetoException;

import javax.inject.Inject;
import javax.sql.DataSource;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.apache.commons.configuration.Configuration;

import com.google.inject.Provider;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import edu.dfci.cccb.mev.common.domain.guice.c3p0.annotation.Persistence;

/**
 * Provides a pooled DataSource
 * 
 * @author levk
 * @since CRYSTAL
 */
@ToString
@EqualsAndHashCode
@Log4j
public class PooledDataSourceProvider implements Provider<DataSource> {

  public static final String DRIVER = "database.driver.class";
  public static final String URL = "database.url";
  public static final String USER = "database.user";
  public static final String PASSWORD = "database.password";

  private @Inject @Persistence Configuration configuration;

  /* (non-Javadoc)
   * @see com.google.inject.Provider#get() */
  @Override
  public DataSource get () {
    try {
      ComboPooledDataSource dataSource = new ComboPooledDataSource ();

      String driver = configuration.getString (DRIVER);
      String url = configuration.getString (URL);
      String user = configuration.getString (USER);
      String password = configuration.getString (PASSWORD);

      dataSource.setDriverClass (driver);
      dataSource.setJdbcUrl (url);
      dataSource.setUser (user);
      dataSource.setPassword (password);

      log.info ("Setting up DataSource using " + driver + " at " + url + " for "
                + user + ":" + (log.isDebugEnabled () ? password : "<redacted>"));

      return dataSource;
    } catch (PropertyVetoException e) {
      throw new IllegalArgumentException (e);
    }
  }
}
