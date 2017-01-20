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
package edu.dfci.cccb.mev.web.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import edu.dfci.cccb.mev.edger.rest.EdgeConfiguration;
import edu.dfci.cccb.mev.normalization.rest.NormalizationConfiguration;
import edu.dfci.cccb.mev.wgcna.rest.configuration.WgcnaConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import edu.dfci.cccb.mev.annotation.server.configuration.AnnotationServerConfiguration;
import edu.dfci.cccb.mev.annotation.server.configuration.ProbeAnnotationsConfigurationMain;
import edu.dfci.cccb.mev.anova.rest.configuration.AnovaRestConfiguration;
import edu.dfci.cccb.mev.dataset.rest.configuration.DatasetRestConfiguration;
import edu.dfci.cccb.mev.dataset.rest.configuration.RDispatcherConfiguration;
import edu.dfci.cccb.mev.deseq.rest.configuration.DESeqRestConfiguration;
import edu.dfci.cccb.mev.genemad.rest.configuration.GeneMADAnalysisConfiguration;
import edu.dfci.cccb.mev.genesd.rest.configuration.GeneSDAnalysisConfiguration;
import edu.dfci.cccb.mev.geods.rest.configuration.GeoDatasetsConfigurationMain;
import edu.dfci.cccb.mev.gsea.rest.GseaConfiguration;
import edu.dfci.cccb.mev.hcl.rest.configuration.HclRestConfiguration;
import edu.dfci.cccb.mev.histogram.rest.configuration.HistogramAnalysisConfiguration;
import edu.dfci.cccb.mev.kmeans.rest.configuration.KMeansRestConfiguration;
import edu.dfci.cccb.mev.limma.rest.configuration.LimmaRestConfiguration;
import edu.dfci.cccb.mev.nmf.rest.configuration.NmfRestConfiguration;
import edu.dfci.cccb.mev.pca.rest.configuration.PcaConfiguration;
import edu.dfci.cccb.mev.pe.rest.configuration.PathwayEnrichmentConfiguration;
import edu.dfci.cccb.mev.presets.rest.configuration.PresetsRestConfiguration;
import edu.dfci.cccb.mev.stats.rest.configuration.StatsRestConfiguration;
import edu.dfci.cccb.mev.survival.rest.configuration.SurvivalAnalysisConfiguration;
import edu.dfci.cccb.mev.t_test.rest.configuration.TTestRestConfiguration;
import edu.dfci.cccb.mev.topgo.rest.configuration.TopGoConfiguration;
import edu.dfci.cccb.mev.voom.rest.configuration.VoomConfiguration;
import edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations;

/**
 * @author levk
 * 
 */
public class ApplicationInitializer implements WebApplicationInitializer {

  /* (non-Javadoc)
   * @see
   * org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet
   * .ServletContext) */
  @Override
  public void onStartup (ServletContext servletContext) throws ServletException {
    AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext ();

    mvcContext.register (DispatcherConfiguration.class, PersistenceConfiguration.class, ContainerConfigurations.class);

    mvcContext.register (RDispatcherConfiguration.class);
    mvcContext.register (DatasetRestConfiguration.class);
    mvcContext.register (PresetsRestConfiguration.class);
    mvcContext.register (GeoDatasetsConfigurationMain.class);
    mvcContext.register (ProbeAnnotationsConfigurationMain.class);
    mvcContext.register (AnnotationServerConfiguration.class);
    mvcContext.register (HclRestConfiguration.class);
    mvcContext.register (LimmaRestConfiguration.class);
    mvcContext.register (KMeansRestConfiguration.class);
    mvcContext.register (AnovaRestConfiguration.class);
    mvcContext.register (TTestRestConfiguration.class);
    mvcContext.register (StatsRestConfiguration.class);
    mvcContext.register (DESeqRestConfiguration.class);
    mvcContext.register (NmfRestConfiguration.class);
    mvcContext.register (SurvivalAnalysisConfiguration.class);
    mvcContext.register (TopGoConfiguration.class);
    mvcContext.register (PcaConfiguration.class);
    mvcContext.register (VoomConfiguration.class);
    mvcContext.register (HistogramAnalysisConfiguration.class);
    mvcContext.register (GeneSDAnalysisConfiguration.class);
    mvcContext.register (GeneMADAnalysisConfiguration.class);
    mvcContext.register (PathwayEnrichmentConfiguration.class);
    mvcContext.register (GseaConfiguration.class);
    mvcContext.register (NormalizationConfiguration.class);
    mvcContext.register (EdgeConfiguration.class);
    mvcContext.register (WgcnaConfiguration.class);

    DispatcherServlet dispatcher = new DispatcherServlet (mvcContext);

    Dynamic registration = servletContext.addServlet ("dispatcher", dispatcher);

    final String sessionInterval = System.getenv ("SESSION_INTERVAL");
    if (sessionInterval != null)
      servletContext.addListener (new HttpSessionListener () {
        private final int interval = Integer.parseInt (sessionInterval);

        @Override
        public void sessionDestroyed (HttpSessionEvent se) {}

        @Override
        public void sessionCreated (HttpSessionEvent se) {
          se.getSession ().setMaxInactiveInterval (interval);
        }
      });

    registration.setLoadOnStartup (1);
    registration.addMapping ("/");
  }
}
