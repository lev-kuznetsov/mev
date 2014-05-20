package edu.dfci.cccb.mev.t_test.rest.controllers;

import static edu.dfci.cccb.mev.dataset.rest.resolvers.DatasetPathVariableMethodArgumentResolver.DATASET_URL_ELEMENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.DatasetException;
import edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type;
import edu.dfci.cccb.mev.t_test.domain.contract.TTestBuilder;

@RestController
@RequestMapping ("/dataset/" + DATASET_URL_ELEMENT)
@Scope (SCOPE_REQUEST)
public class TTestAnalysisController {

  private @Getter @Setter @Inject Dataset dataset;
  private @Getter @Setter @Inject @Named ("one.sample.t-test.builder") TTestBuilder oneSampleTTestBuilder;
  private @Getter @Setter @Inject @Named ("two.sample.t-test.builder") TTestBuilder twoSampleTTestBuilder;
  private @Getter @Setter @Inject @Named ("paired.t-test.builder") TTestBuilder pairedTTestBuilder;

  /*
   * t-test for the one-sample analysis
   */
  
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
public static class OneSampleTTestDTO{
  @JsonProperty @Getter private String name;  
  @JsonProperty @Getter private String experimentName;  
  @JsonProperty @Getter private double pValue;
  @JsonProperty @Getter private boolean multTestCorrection;  
  @JsonProperty @Getter private double userMean;
}
@RequestMapping (value = "/analyze/one_sample_ttest", method = POST)
@ResponseStatus (OK)
public void startOneSampleJson (@RequestBody OneSampleTTestDTO dto) throws DatasetException {
dataset.analyses ().put (oneSampleTTestBuilder.name (dto.name())
                 .dataset (dataset)
                 .controlSelection (dataset.dimension (Type.COLUMN).selections ().get (dto.experimentName ()))
                 .pValue (dto.pValue())
                 .multipleTestCorrectionFlag (dto.multTestCorrection())
                 .oneSampleMean (dto.userMean())
                 .build ());
}
  
  
  /*
   * t-test for the two-sample analysis
   */
  
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
public static class TwoSampleTTestDTO{
  @JsonProperty @Getter private String name;  
  @JsonProperty @Getter private String experimentName;
  @JsonProperty @Getter private String controlName;
  @JsonProperty @Getter private double pValue;
  @JsonProperty @Getter private boolean multTestCorrection;
  @JsonProperty @Getter private boolean assumeEqualVariance;  
}
@RequestMapping (value = "/analyze/two_sample_ttest",
  method = POST)
@ResponseStatus (OK)
public void startTwoSampleJson (@RequestBody TwoSampleTTestDTO dto) throws DatasetException {
dataset.analyses ().put (twoSampleTTestBuilder.name (dto.name ())
                 .dataset (dataset)
                 .experimentSelection (dataset.dimension (Type.COLUMN).selections ().get (dto.experimentName ()))
                 .controlSelection (dataset.dimension (Type.COLUMN).selections ().get (dto.controlName ()))
                 .pValue (dto.pValue())
                 .equalVarianceFlag (dto.assumeEqualVariance())
                 .multipleTestCorrectionFlag (dto.multTestCorrection())
                 .build ());
}

  
/*
   * t-test for the paired analysis
  */
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
public static class PairedSampleTTestDTO{
  @JsonProperty @Getter private String name;  
  @JsonProperty @Getter private String experimentName;
  @JsonProperty @Getter private String controlName;
  @JsonProperty @Getter private double pValue;
  @JsonProperty @Getter private boolean multTestCorrection;
}
@RequestMapping (value = "/analyze/paired_ttest",
method = POST)
@ResponseStatus (OK)
public void startPaired(@RequestBody PairedSampleTTestDTO dto) throws DatasetException {
dataset.analyses ().put (pairedTTestBuilder.name (dto.name())
               .dataset (dataset)
               .experimentSelection (dataset.dimension (Type.COLUMN).selections ().get (dto.experimentName ()))
               .controlSelection (dataset.dimension (Type.COLUMN).selections ().get (dto.controlName ()))
               .pValue (dto.pValue ())
               .multipleTestCorrectionFlag (dto.multTestCorrection())
               .build ());
}
  
  
}
