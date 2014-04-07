      <form role="form">

          <div class="form-group">
            <label for="tTestName" class="control-label">Analysis Name</label>

            <input id="tTestName" ng-model="params.name" placeholder="Ex: My_Analysis_1">
          </div>

          <div class="form-group">
            <label for="tTestType" class="control-label">Type</label>
            <select id="tTestType" ng-options="sampleType.label for sampleType in options.sampleTypes" ng-model="params.sampleType"></select>
          </div>

		  <div class="form-group">		    
		    <label for="tTestAnalysisExperiment" class="control-label">Experiment</label>
		      <select id="tTestAnalysisExperiment" ng-options="selection.name for selection in heatmapData.column.selections" ng-model="params.analysisExperiment">
		      </select>		      
		  </div>
    
          <div class="form-group" ng-show="!isOneSample()">
		    <label for="tTestAnalysisControl" class="control-label">Control</label>		
		      <select id="tTestAnalysisControl"  ng-options="selection.name for selection in heatmapData.column.selections" ng-model="params.analysisControl">
		      </select>		
		  </div>
		  
		  <div class="form-group">
		    <label for="tTestAnalysisPValue" class="control-label">P-Value</label>
		      <input type="number" min="0.01" max="0.1" step=".01" id="tTestAnalysisPValue" ng-model="params.pValue" class="input-small" />
		  </div>
		  
		  <div class="form-group" ng-show="isOneSample()">
		    <label for="tTestAnalysisUserMean" class="control-label" >Mean</label>		    
		    <input type="number" step=".01" id="tTestAnalysisUserMean" ng-model="params.userMean" placeholder="Ex: 0.55" class="input-small" />		    
		  </div>	    
		  		  
		  <div class="form-group">
		    <label for="tTestAnalysisMultitestCorrection" class="control-label" >Multitest correction:</label>		    
		    <input type="checkbox" id="tTestAnalysisMultitestCorrection" ng-model="params.multitestCorrection" class="input-small"/>		    
		  </div>	    
		  
		  <div class="form-group" ng-show="isTwoSample()">
		    <label for="tTestAnalysisAssumeEqualVariance" class="control-label" >Equal variance:</label>		    
		    <input type="checkbox" id="tTestAnalysisAssumeEqualVariance" ng-model="params.assumeEqualVariance" class="input-small"/>		    
		  </div>	    
		  	    
     </form> 
     		
     <button class="btn btn-success btn-block" ng-click="testInit()" data-dismiss="modal" aria-hidden="true">Analyze</button>