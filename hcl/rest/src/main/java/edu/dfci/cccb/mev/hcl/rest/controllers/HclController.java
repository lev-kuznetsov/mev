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
package edu.dfci.cccb.mev.hcl.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.DATASET_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.context.RestPathVariableDatasetRequestContextInjector.DIMENSION_URL_ELEMENT;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension;
import edu.dfci.cccb.mev.hcl.domain.contract.Hcl;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;

/**
 * @author levk
 * 
 */
@RestController
@RequestMapping ("/analysis/" + DATASET_URL_ELEMENT + "/hcl/" + DIMENSION_URL_ELEMENT)
public class HclController {

  private @Inject Dataset dataset;
  private @Inject Dimension dimension;
  private @Inject Hcl hcl;

  @RequestMapping (method = POST)
  public Node cluster () throws DatasetException {
    return hcl.cluster (dataset, dimension, null, null);
  }
}
