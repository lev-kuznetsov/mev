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

package edu.dfci.cccb.mev.common.domain.guice;

import static com.google.inject.name.Names.named;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.ToString;

import com.google.inject.AbstractModule;

/**
 * @author levk
 */
@ToString
public class TestModule extends AbstractModule {

  private @Getter @Inject @Named ("hello") String hello;
  private @Getter @Inject @Named ("other") String other;

  /* (non-Javadoc)
   * @see com.google.inject.AbstractModule#configure() */
  @Override
  protected void configure () {
    bind (String.class).annotatedWith (named ("helloAndOther")).toInstance (hello + other);
  }
}
