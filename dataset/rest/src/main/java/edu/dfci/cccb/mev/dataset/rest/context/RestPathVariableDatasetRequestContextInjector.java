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

import static edu.dfci.cccb.mev.dataset.domain.contract.Analysis.VALID_ANALYSIS_NAME_REGEX;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dataset.VALID_DATASET_NAME_REGEX;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.from;
import static edu.dfci.cccb.mev.dataset.domain.contract.Selection.VALID_SELECTION_NAME_REGEX;
import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import java.util.Map;

import lombok.ToString;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

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
@ToString
public class RestPathVariableDatasetRequestContextInjector {

  public static final String DATASET = "dataset";
  public static final String DIMENSION = "dimension";
  public static final String SELECTION = "selection";
  public static final String ANALYSIS = "analysis";

  public static final String DATASET_URL_ELEMENT = "{" + DATASET + ":" + VALID_DATASET_NAME_REGEX + "}";
  public static final String DIMENSION_URL_ELEMENT = "{" + DIMENSION + "}";
  public static final String SELECTION_URL_ELEMENT = "{" + SELECTION + ":" + VALID_SELECTION_NAME_REGEX + "}";
  public static final String ANALYSIS_URL_ELEMENT = "{" + ANALYSIS + ":" + VALID_ANALYSIS_NAME_REGEX + "}";

  @Bean
  @Scope (value = SCOPE_SESSION, proxyMode = INTERFACES)
  public Workspace workspace () {
    return new ArrayListWorkspace ();
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Dataset dataset (Workspace workspace, NativeWebRequest request) throws DatasetNotFoundException,
                                                                        MissingPathVariableException {
    return workspace.get (variable (DATASET, request));
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Dimension dimension (Dataset dataset, NativeWebRequest request) throws InvalidDimensionTypeException,
                                                                        MissingPathVariableException {
    return dataset.dimension (from (variable (DIMENSION, request)));
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Selection selections (Dimension dimension, NativeWebRequest request) throws SelectionNotFoundException,
                                                                             MissingPathVariableException {
    return dimension.selections ().get (variable (SELECTION, request));
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Annotation annoation (Dimension dimension) {
    return dimension.annotation ();
  }

  @Bean
  @Scope (value = SCOPE_REQUEST, proxyMode = INTERFACES)
  public Analysis analysis (Dataset dataset, NativeWebRequest request) throws AnalysisNotFoundException,
                                                                      MissingPathVariableException {
    return dataset.analyses ().get (variable (ANALYSIS, request));
  }

  @SuppressWarnings ("unchecked")
  private String variable (String name, NativeWebRequest request) throws MissingPathVariableException {
    String value = ((Map<String, String>) request.getAttribute (URI_TEMPLATE_VARIABLES_ATTRIBUTE,
                                                                RequestAttributes.SCOPE_REQUEST)).get (name);
    if (value == null)
      throw new MissingPathVariableException ().variable (name);
    return value;
  }
}
