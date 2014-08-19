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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ExceptionMapper;

import com.google.inject.Binder;
import com.google.inject.Module;

import edu.dfci.cccb.mev.common.domain.guice.SingletonModule;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ContentNegotiationConfigurer;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ContentNegotiationMapper;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ExceptionBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.MessageReaderBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.MessageWriterBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ResourceBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceBinder;
import edu.dfci.cccb.mev.common.services.guice.jaxrs.ServiceModule;
import edu.dfci.cccb.mev.common.services.support.mappers.MevExceptionMapper;
import edu.dfci.cccb.mev.common.services.support.messages.JacksonMessageWriter;

/**
 * Enables the MeV RESTful services
 * 
 * @author levk
 * @since CRYSTAL
 */
public class MevServiceModule implements Module {
  public static final String SERVICE_URI = "/services/*";

  public static final MediaType TEXT_TSV_TYPE = new MediaType ("text", "tab-separated-values");
  public static final String TEXT_TSV = "text/tab-separated-values";

  private static final String PARAMETER = "format";

  /* (non-Javadoc)
   * @see com.google.inject.Module#configure(com.google.inject.Binder) */
  @Override
  public final void configure (Binder binder) {
    binder.install (new ServiceModule () {
      @Override
      public void configure (ContentNegotiationConfigurer content) {
        MevServiceModule.this.configure (content.parameter (PARAMETER));
      }

      @Override
      public void configure (ExceptionBinder binder) {
        MevServiceModule.this.configure (binder);
      }

      @Override
      public void configure (MessageReaderBinder binder) {
        MevServiceModule.this.configure (binder);
      }

      @Override
      public void configure (MessageWriterBinder binder) {
        MevServiceModule.this.configure (binder);
      }

      @Override
      public void configure (ResourceBinder binder) {
        MevServiceModule.this.configure (binder);
      }
    });

    binder.install (new SingletonModule () {

      @Override
      public void configure (Binder binder) {
        binder.install (new ServiceModule () {
          @Override
          public void configure (ContentNegotiationConfigurer content) {
            content.parameter (PARAMETER)
                   .map ("json", APPLICATION_JSON_TYPE)
                   .map ("tsv", TEXT_TSV_TYPE);
          }

          @Override
          public void configure (MessageWriterBinder binder) {
            binder.use (JacksonMessageWriter.class);
          }

          @Override
          public void configure (ServiceBinder binder) {
            binder.service (SERVICE_URI);
          }

          @SuppressWarnings ("unchecked")
          @Override
          public void configure (ExceptionBinder binder) {
            binder.use ((Class<? extends ExceptionMapper<?>>) MevExceptionMapper.class);
          }
        });
      }
    });
  }

  /**
   * @see ServiceModule#configure(ExceptionBinder)
   */
  public void configure (ExceptionBinder binder) {}

  /**
   * @see ServiceModule#configure(MessageReaderBinder)
   */
  public void configure (MessageReaderBinder binder) {}

  /**
   * @see ServiceModule#configure(MessageWriterBinder)
   */
  public void configure (MessageWriterBinder binder) {}

  /**
   * @see ServiceModule#configure(ResourceBinder)
   */
  public void configure (ResourceBinder binder) {}

  /**
   * @see ServiceModule#configure(ContentNegotiationConfigurer)
   */
  public void configure (ContentNegotiationMapper content) {}
}
