<form role="form">

  <div class="form-group">
    <label for="limmaAnalysisName" class="control-label">Name</label>
        <input id="limmaAnalysisName" ng-model="analysisName" placeholder="Ex: My_Analysis_1">
  </div>

  <!--
  <div class="form-group">
    <label for="limmaAnalysisDimension" class="control-label">Dimension</label>

      <select id="limmaAnalysisDimension" ng-model="analysisDimension" ng-options="dimension.name for dimension in dimensions">
      </select>

  </div>
  -->

  <div class="form-group">
    <label for="limmaAnalysisControl" class="control-label">Control</label>

      <select id="limmaAnalysisControl" ng-model="analysisControl" ng-options="selection.name for selection in heatmapData.column.selections">
      </select>

  </div>
  
  <div class="form-group">
    <label for="limmaAnalysisExperiment" class="control-label">Experiment</label>
      <select id="limmaAnalysisExperiment" ng-model="analysisExperiment" ng-options="selection.name for selection in heatmapData.column.selections">
      </select>
      
  </div>
  
</form>

<button class="btn btn-success btn-block" ng-click="testInit()" data-dismiss="modal" aria-hidden="true">Analyze</button>
    