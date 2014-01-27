<form class="form-horizontal">

  <div class="control-group">
    <label for="inputAnalysisName" class="control-label">Name</label>
    <div class="controls">
        <input id="limmaAnalysisName" ng-model="analysisName" placeholder="Ex: My_Analysis_1">
    </div>
  </div>

  <div class="control-group">
    <label for="" class="control-label">Dimension</label>
    <div class="controls">
      <select id="limmaAnalysisDimension" ng-model="analysisDimension" ng-options="dimension.name for dimension in dimensions">
        <option value="">Choose Dimension </option>
      </select>
    </div>
  </div>

  <div class="control-group">
    <label for="" class="control-label">Control</label>
    <div class="controls">
      <select id="limmaAnalysisControl" ng-model="analysisControl" ng-options="selection for selection in selections">
      </select>
    </div>
  </div>
  
  <div class="control-group">
    <label for="" class="control-label">Experiment</label>
    <div class="controls">
      <select id="limmaAnalysisExperiment" ng-model="analysisExperiment" ng-options="selection for selection in selections">
        
      </select>
    </div>
  </div>
  
</form>

<button class="btn btn-success btn-block" ng-click="limmaInit()" data-dismiss="modal" aria-hidden="true">Analyze</button>
    