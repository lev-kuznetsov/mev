
<div class="container-fluid">

	<form class="form-horizontal">
	
	  <div class="form-group">
	    <label for="kMeansAnalysisName" class="control-label">Name:</label>
	        <input id="kMeansAnalysisName" ng-model="params.analysisName">
	  </div>
	
	  <div class="form-group">
	    <label for="kMeansAnalysisDimension" class="control-label">Dimension:</label>
	      <select id="kMeansAnalysisDimension" ng-model="params.analysisDimension" 
	      	ng-options="dimension.name for dimension in options.dimensions">
	      </select>
	  </div>
	
	  <div class="form-group">
	    <label for="kMeansAnalysisClusters" class="control-label">Clusters:</label>
	      <select id="kMeansAnalysisClusters" ng-model="params.analysisClusters" 
	      	ng-options="selection for selection in options.clusters">
	      </select>
	  </div>
	  
	  <div class="form-group">
	    <label for="kMeansAnalysisMetric" class="control-label">Distance Metric:</label>
	      <select id="kMeansAnalysisMetric" ng-model="params.analysisMetric" ng-options="selection.name for selection in options.metrics">
	      </select>
	  </div>
	  
	  <div class="form-group">
	    <label for="kMeansAnalysisIterations" class="control-label">Iterations:</label>
	      <select id="kMeansAnalysisIterations" ng-model="params.analysisIterations" 
	      	ng-options="selection for selection in options.iterations">
	      </select>
	  </div>
	
	</form>
	
	<button class="btn btn-success btn-block" ng-click="testInit()" data-dismiss="modal" aria-hidden="true">Analyze</button>

</div>
