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

package edu.dfci.cccb.mev.common.test.jetty;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import lombok.Getter;

@WebListener
public class TestListener implements ServletContextListener {

  private static volatile @Getter boolean active = false;

  /* (non-Javadoc)
   * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
   * ServletContextEvent) */
  @Override
  public void contextDestroyed (ServletContextEvent arg0) {
    synchronized (getClass ()) {
      active = false;
    }
  }

  /* (non-Javadoc)
   * @see
   * javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
   * ServletContextEvent) */
  @Override
  public void contextInitialized (ServletContextEvent arg0) {
    synchronized (getClass ()) {
      active = true;
    }
  }
}
