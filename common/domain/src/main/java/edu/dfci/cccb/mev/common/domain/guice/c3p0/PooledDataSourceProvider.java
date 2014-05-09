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
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import com.google.inject.Provider;
import com.mchange.v2.c3p0.ComboPooledDataSource;

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

  public static final String DRIVER_CLASS = "database.driver.class";
  public static final String URL = "database.url";
  public static final String USER = "database.user";
  public static final String PASSWORD = "database.password";

  private @Inject @Named (DRIVER_CLASS) String driver;
  private @Inject @Named (URL) String url;
  private @Inject @Named (USER) String user;
  private @Inject @Named (PASSWORD) String password;

  /* (non-Javadoc)
   * @see com.google.inject.Provider#get() */
  @Override
  public DataSource get () {
    try {
      ComboPooledDataSource dataSource = new ComboPooledDataSource ();

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
