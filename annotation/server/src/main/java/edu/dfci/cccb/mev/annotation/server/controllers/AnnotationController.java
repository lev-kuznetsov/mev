package edu.dfci.cccb.mev.annotation.server.controllers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import com.google.refine.RefineServlet;

@RestController
@RequestMapping("/annotation/")
@Log4j
public class AnnotationController extends WebApplicationObjectSupport {

	private RefineServlet refineServlet;
	private @Inject Environment environment;

	@PostConstruct
	private void createRefineServlet() throws ServletException {
		refineServlet = new RefineServlet();
		refineServlet.init(new DelegatingServletConfig());
	}

	@RequestMapping("**")
	public void handleAnnotation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug ("Handling annotation request");
		HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request){
			@Override			
			public String getPathInfo() {
				//System.out.println("MySrevelet******:"+super.getServletPath());
				return super.getServletPath().replace("/annotation", "");
			}
		};
		
		this.refineServlet.service(wrappedRequest, response);
	}

	/**
	 * Internal implementation of the ServletConfig interface, to be passed to
	 * the wrapped servlet. Delegates to ServletWrappingController fields and
	 * methods to provide init parameters and other environment info.
	 */
	private class DelegatingServletConfig implements ServletConfig {

		private Properties properties;

		{
			properties = new Properties();
			properties.setProperty("refine.version",
					environment.getProperty("refine.version", "$VERSION"));
			properties.setProperty("refine.revision",
					environment.getProperty("refine.revision", "$REVISION"));
			properties.setProperty(
					"refine.data",
					environment.getProperty("refine.data",
							System.getProperty("java.io.tmpdir")));
		}

		public String getServletName() {
			return "refine";
		}

		public ServletContext getServletContext() {
			return AnnotationController.this.getServletContext();
		}

		public String getInitParameter(String paramName) {
			return properties.getProperty(paramName);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Enumeration getInitParameterNames() {
			return properties.keys();
		}
	}
}
