package edu.dfci.cccb.mev.anova.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static edu.dfci.cccb.mev.dataset.rest.resolvers.DimensionPathVariableMethodArgumentResolver.DIMENSION_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.experimental.Accessors;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dfci.cccb.mev.anova.domain.contract.AnovaBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.Analysis;
import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;

@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class AnovaAnalysisController {

  private @Getter @Setter @Inject Dataset dataset;
  private @Getter @Setter @Inject AnovaBuilder anovaBuilder;

  /* t-test for the one-sample analysis */
  @RequestMapping (value = "/analyze/anova/{name}(dimension="
                           + DIMENSION_URL_ELEMENT
                           + ",pval={pval}"
                           + ",mtc={mtc})",
                   method = POST)
  @ResponseStatus (OK)
  public Analysis startOneSample (final @PathVariable ("name") String name,
                              final @PathVariable ("pval") double pValue,
                              final @PathVariable ("mtc") boolean multTestCorrection,
                              final @RequestBody String[] sampleGroups) throws DatasetException {
    return anovaBuilder.name (name)
               .dataset (dataset)
               .groupSelections (sampleGroups)
               .pValue (pValue)
               .multipleTestCorrectionFlag (multTestCorrection)
               .buildAsync ();
  }


  @NoArgsConstructor
  @AllArgsConstructor
  @Accessors(fluent=true)
  public static class AnovaDTO{
    @JsonProperty
    @Getter private String name;
    @JsonProperty @Getter private double pValue;
    @JsonProperty @Getter private boolean multTestCorrection;
    @JsonProperty @Getter private String[] selections;
  }
  @RequestMapping (value = "/analyze/anova/{name}",
          method = PUT)
  @ResponseStatus (OK)
  public Analysis putTwoSampleJson (@RequestBody AnovaDTO params) throws DatasetException {
    return anovaBuilder.name (params.name())
            .dataset (dataset)
            .groupSelections (params.selections())
            .pValue (params.pValue())
            .multipleTestCorrectionFlag (params.multTestCorrection())
            .buildAsync ();
  }

}
