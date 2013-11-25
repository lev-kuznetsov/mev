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
package edu.dfci.cccb.mev.api.client.support.view;

import static java.lang.Thread.currentThread;

import java.util.ConcurrentModificationException;
import java.util.Map;

import javax.servlet.ServletContext;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Synchronized;
import lombok.ToString;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.velocity.VelocityView;

import edu.dfci.cccb.mev.api.client.support.view.ViewBuilders.AbstractViewBuilder.AbstractUrlBasedViewBuilder.AbstractTemplateViewBuilder.FreeMarkerViewBuilder;
import edu.dfci.cccb.mev.api.client.support.view.ViewBuilders.AbstractViewBuilder.AbstractUrlBasedViewBuilder.AbstractTemplateViewBuilder.VelocityViewBuilder;
import edu.dfci.cccb.mev.api.client.support.view.ViewBuilders.AbstractViewBuilder.AbstractUrlBasedViewBuilder.InternalResourceViewBuilder;
import edu.dfci.cccb.mev.api.client.support.view.ViewBuilders.AbstractViewBuilder.AbstractUrlBasedViewBuilder.InternalResourceViewBuilder.JstlViewBuilder;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;

/**
 * @author levk
 * 
 */
public class ViewBuilders {

  @ToString
  @EqualsAndHashCode
  @SuppressWarnings ("unchecked")
  public abstract static class AbstractViewBuilder <B extends AbstractViewBuilder<?>> {

    private Thread transactionThread = null;

    private @Getter Map<String, ?> attributes;
    private @Getter ApplicationContext applicationContext;
    private @Getter String beanName;
    private @Getter String contentType;
    private @Getter boolean exposePathVariables = true;
    private @Getter String requestContextAttribute;
    private @Getter ServletContext servletContext;
    
    protected void transactionIn () {
      if (transactionThread == null)
        transactionThread = currentThread ();
      else if (!currentThread ().equals (transactionThread))
        throw new ConcurrentModificationException ();
    }
    
    protected void transactionOut () {
      if (transactionThread != null) {
        if (transactionThread != currentThread ())
          throw new ConcurrentModificationException ();
        transactionThread = null;
      }
    }

    @Synchronized
    public B attributes (Map<String, ?> attributes) {
      transactionIn ();
      this.attributes = attributes;
      return (B) this;
    }

    @Synchronized
    public B applicationContext (ApplicationContext applicationContext) {
      transactionIn ();
      this.applicationContext = applicationContext;
      return (B) this;
    }

    @Synchronized
    public B beanName (String beanName) {
      transactionIn ();
      this.beanName = beanName;
      return (B) this;
    }

    @Synchronized
    public B contentType (String contentType) {
      transactionIn ();
      this.contentType = contentType;
      return (B) this;
    }

    @Synchronized
    public B exposePathVariables (boolean exposePathVariables) {
      transactionIn ();
      this.exposePathVariables = exposePathVariables;
      return (B) this;
    }

    @Synchronized
    public B requestContextAttribute (String requestContextAttribute) {
      transactionIn ();
      this.requestContextAttribute = requestContextAttribute;
      return (B) this;
    }

    @Synchronized
    public B servletContext (ServletContext servletContext) {
      transactionIn ();
      this.servletContext = servletContext;
      return (B) this;
    }

    protected <T extends AbstractView> T initialize (T view) {
      if (attributes != null)
        view.setAttributesMap (attributes);
      if (applicationContext != null)
        view.setApplicationContext (applicationContext);
      if (beanName != null)
        view.setBeanName (beanName);
      if (contentType != null)
        view.setContentType (contentType);
      view.setExposePathVariables (exposePathVariables);
      if (requestContextAttribute != null)
        view.setRequestContextAttribute (requestContextAttribute);
      if (servletContext != null)
        view.setServletContext (servletContext);
      return view;
    }

    @ToString
    @EqualsAndHashCode (callSuper = true)
    public abstract static class AbstractUrlBasedViewBuilder <B extends AbstractUrlBasedViewBuilder<?>> extends AbstractViewBuilder<B> {
      private @Getter String url;

      @Synchronized
      public B url (String url) {
        transactionIn ();
        this.url = url;
        return (B) this;
      }

      protected <T extends AbstractUrlBasedView> T initialize (T view) {
        super.initialize (view);
        if (url != null)
          view.setUrl (url);
        return view;
      }

      @ToString
      @EqualsAndHashCode (callSuper = true)
      public abstract static class AbstractTemplateViewBuilder <B extends AbstractTemplateViewBuilder<?>> extends AbstractUrlBasedViewBuilder<B> {
        private @Getter boolean allowRequestOverride = false;
        private @Getter boolean allowSessionOverride = false;
        private @Getter boolean exposeRequestAttributes = false;
        private @Getter boolean exposeSessionAttributes = false;
        private @Getter boolean exposeSpringMacroHelpers = true;

        @Synchronized
        public B allowRequestOverride (boolean allowRequestOverride) {
          transactionIn ();
          this.allowRequestOverride = allowRequestOverride;
          return (B) this;
        }

        @Synchronized
        public B allowSessionOverride (boolean allowSessionOverride) {
          transactionIn ();
          this.allowSessionOverride = allowSessionOverride;
          return (B) this;
        }

        @Synchronized
        public B exposeRequestAttributes (boolean exposeRequestAttributes) {
          transactionIn ();
          this.exposeRequestAttributes = exposeRequestAttributes;
          return (B) this;
        }

        @Synchronized
        public B exposeSessionAttributes (boolean exposeSessionAttributes) {
          transactionIn ();
          this.exposeSessionAttributes = exposeSessionAttributes;
          return (B) this;
        }

        @Synchronized
        public B exposeSpringMacroHelpers (boolean exposeSpringMacroHelpers) {
          transactionIn ();
          this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
          return (B) this;
        }

        protected <T extends AbstractTemplateView> T initialize (T view) {
          super.initialize (view);
          view.setAllowRequestOverride (allowRequestOverride);
          view.setAllowSessionOverride (allowSessionOverride);
          view.setExposeRequestAttributes (exposeRequestAttributes);
          view.setExposeSessionAttributes (exposeSessionAttributes);
          view.setExposeSpringMacroHelpers (exposeSpringMacroHelpers);
          return view;
        }

        @ToString
        @EqualsAndHashCode (callSuper = true)
        public static class FreeMarkerViewBuilder extends AbstractTemplateViewBuilder<FreeMarkerViewBuilder> {
          private @Getter String encoding;
          private @Getter Configuration configuration;

          @Synchronized
          public FreeMarkerViewBuilder encoding (String encoding) {
            transactionIn ();
            this.encoding = encoding;
            return this;
          }

          @Synchronized
          public FreeMarkerViewBuilder configuration (Configuration configuration) {
            transactionIn ();
            this.configuration = configuration;
            return this;
          }

          @Synchronized
          public FreeMarkerView build () {
            transactionOut ();
            FreeMarkerView view = new FreeMarkerView ();
            super.initialize (view);
            if (encoding != null)
              view.setEncoding (encoding);
            if (configuration != null)
              view.setConfiguration (configuration);
            
            view.addStaticAttribute ("enums", BeansWrapper.getDefaultInstance().getEnumModels());
            return view;
          }
        }

        @ToString
        @EqualsAndHashCode (callSuper = true)
        public static class VelocityViewBuilder extends AbstractTemplateViewBuilder<VelocityViewBuilder> {
          private @Getter boolean cacheTemplate = false;
          private @Getter String dateToolAttribute;
          private @Getter String encoding;
          private @Getter String numberToolAttribute;
          private @Getter @SuppressWarnings ("rawtypes") Map<String, Class> toolAttributes;
          private @Getter VelocityEngine velocityEngine;

          @Synchronized
          public VelocityViewBuilder cacheTemplate (boolean cacheTemplate) {
            transactionIn ();
            this.cacheTemplate = cacheTemplate;
            return this;
          }
          
          @Synchronized
          public VelocityViewBuilder dateToolAttribute (String dateToolAttribute) {
            transactionIn ();
            this.dateToolAttribute = dateToolAttribute;
            return this;
          }
          
          @Synchronized
          public VelocityViewBuilder encoding (String encoding) {
            transactionIn ();
            this.encoding = encoding;
            return this;
          }

          @Synchronized
          public VelocityView build () {
            transactionOut ();
            VelocityView view = new VelocityView ();
            super.initialize (view);
            view.setCacheTemplate (cacheTemplate);
            if (dateToolAttribute != null)
              view.setDateToolAttribute (dateToolAttribute);
            if (encoding != null)
              view.setEncoding (encoding);
            if (numberToolAttribute != null)
              view.setNumberToolAttribute (numberToolAttribute);
            if (toolAttributes != null)
              view.setToolAttributes (toolAttributes);
            if (velocityEngine != null)
              view.setVelocityEngine (velocityEngine);
            return view;
          }
        }
      }

      @ToString
      @EqualsAndHashCode (callSuper = true)
      public static class InternalResourceViewBuilder <B extends InternalResourceViewBuilder<?>> extends AbstractUrlBasedViewBuilder<B> {
        public @Getter boolean alwaysInclude = false;
        public @Getter boolean exposeContextBeansAsAttributes = false;
        public @Getter String[] exposedContextBeanNames;
        public @Getter boolean preventDispatchLoop;

        @Synchronized
        public B alwaysInclude (boolean alwaysInclude) {
          transactionIn ();
          this.alwaysInclude = alwaysInclude;
          return (B) this;
        }

        @Synchronized
        public B exposeContextBeansAsAttributes (boolean exposeContextBeansAsAttributes) {
          transactionIn ();
          this.exposeContextBeansAsAttributes = exposeContextBeansAsAttributes;
          return (B) this;
        }

        @Synchronized
        public B exposedContextBeanNames (String[] exposedContextBeanNames) {
          transactionIn ();
          this.exposedContextBeanNames = exposedContextBeanNames;
          return (B) this;
        }

        @Synchronized
        public B preventDispatchLoop (boolean preventDispatchLoop) {
          transactionIn ();
          this.preventDispatchLoop = preventDispatchLoop;
          return (B) this;
        }

        protected <T extends InternalResourceView> T initialize (T view) {
          super.initialize (view);
          view.setAlwaysInclude (alwaysInclude);
          view.setExposeContextBeansAsAttributes (exposeContextBeansAsAttributes);
          if (exposedContextBeanNames != null)
            view.setExposedContextBeanNames (exposedContextBeanNames);
          view.setPreventDispatchLoop (preventDispatchLoop);
          return view;
        }

        @Synchronized
        public InternalResourceView build () {
          transactionOut ();
          InternalResourceView view = new InternalResourceView ();
          return initialize (view);
        }

        @ToString
        @EqualsAndHashCode (callSuper = true)
        public static class JstlViewBuilder extends InternalResourceViewBuilder<JstlViewBuilder> {

          @Synchronized
          public JstlView build () {
            transactionOut ();
            JstlView view = new JstlView ();
            return initialize (view);
          }
        }
      }
    }
  }

  public static FreeMarkerViewBuilder freemarker () {
    return new FreeMarkerViewBuilder ();
  }

  public static VelocityViewBuilder velocity () {
    return new VelocityViewBuilder ();
  }

  public static InternalResourceViewBuilder<InternalResourceViewBuilder<?>> internalResource () {
    return new InternalResourceViewBuilder<InternalResourceViewBuilder<?>> ();
  }

  public static JstlViewBuilder jstl () {
    return new JstlViewBuilder ();
  }

  private ViewBuilders () {}
}
