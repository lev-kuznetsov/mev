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

package edu.dfci.cccb.mev.common.services.guice;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.inject.servlet.RequestScoped;

import edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder;

public class TestMevServiceModule extends MevServiceModule {

  @XmlRootElement
  @XmlAccessorType (XmlAccessType.NONE)
  public static class Pojo {
    private final @XmlAttribute String foo;

    public Pojo (String foo) {
      this.foo = foo;
    }
  }

  @Path ("/simple")
  public interface Simple {
    @GET
    Pojo pojo (@QueryParam ("foo") String foo);
  }

  public static class SimpleImpl implements Simple {
    public Pojo pojo (String foo) {
      return new Pojo (foo);
    }
  }

  @Path ("/echo")
  public interface Echo {
    @GET
    String echo (@QueryParam ("word") String word);
  }

  public static class EchoImpl implements Echo {
    public String echo (String echo) {
      return echo;
    }
  }

  @Override
  public void configure (ResourceBinder binder) {
    binder.publish (Simple.class).to (SimpleImpl.class).in (RequestScoped.class);
    binder.publish (Echo.class).to (EchoImpl.class).in (Singleton.class);
  }
}
