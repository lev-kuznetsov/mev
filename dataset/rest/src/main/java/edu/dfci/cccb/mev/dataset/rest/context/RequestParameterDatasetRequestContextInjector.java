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
package edu.dfci.cccb.mev.dataset.rest.context;

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.from;
import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.NativeWebRequest;

import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Annotation;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.dataset.domain.contract.InvalidDimensionTypeException;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.contract.SelectionNotFoundException;
import edu.dfci.cccb.mev.dataset.domain.contract.Workspace;
import edu.dfci.cccb.mev.dataset.domain.simple.ArrayListWorkspace;

/**
 * @author levk
 * 
 */
@Configuration
public class RequestParameterDatasetRequestContextInjector {

  private static final String DATASET_NAME_REQUEST_PARAMETER_NAME = "dataset";
  private static final String DIMENSION_TYPE_REQUEST_PARAMETER_NAME = "dimension";
  private static final String SELECTION_NAME_REQUEST_PARAMETER_NAME = "selection";
  private static final String ANALYSIS_NAME_REQUEST_PARAMETER_NAME = "analysis";

  @Bean
  @Scope (value = SCOPE_SESSION, proxyMode = INTERFACES)
  public Workspace workspace () {
    return new ArrayListWorkspace ();
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Dataset dataset (Workspace workspace, NativeWebRequest request) throws DatasetNotFoundException,
                                                                        MissingRequestParameterException {
    return workspace.get (parameter (DATASET_NAME_REQUEST_PARAMETER_NAME, request));
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Dimension dimension (Dataset dataset, NativeWebRequest request) throws InvalidDimensionTypeException,
                                                                        MissingRequestParameterException {
    return dataset.dimension (from (parameter (DIMENSION_TYPE_REQUEST_PARAMETER_NAME, request)));
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Selection selection (Dimension dimension, NativeWebRequest request) throws SelectionNotFoundException,
                                                                            MissingRequestParameterException {
    return dimension.selections ().get (parameter (SELECTION_NAME_REQUEST_PARAMETER_NAME, request));
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Annotation annotation (Dimension dimension, NativeWebRequest request) {
    return dimension.annotation ();
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Analysis analysis (Dataset dataset, NativeWebRequest request) throws AnalysisNotFoundException,
                                                                      MissingRequestParameterException {
    return dataset.analyses ().get (parameter (ANALYSIS_NAME_REQUEST_PARAMETER_NAME, request));
  }

  private String parameter (String name, NativeWebRequest request) throws MissingRequestParameterException {
    String value = request.getParameter (name);
    if (value == null)
      throw new MissingRequestParameterException (); // TODO: add args
    return value;
  }
}
