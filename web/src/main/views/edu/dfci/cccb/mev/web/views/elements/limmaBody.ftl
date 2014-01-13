<form class="form-horizontal">

  <div class="control-group">
    <label for="inputAnalysisName" class="control-label">Name</label>
    <div class="controls">
        <input id="inputAnalysisName" ng-model="analysisName" placeholder="Ex: My_Analysis_1">
    </div>
  </div>

  <div class="control-group">
    <label for="" class="control-label">Dimension</label>
    <div class="controls">
      <select ng-model="analysisDimension" ng-options="dimension.name for dimension in dimensions">
        <option value="">Choose Dimension </option>
      </select>
    </div>
  </div>

  <div class="control-group">
    <label for="" class="control-label">Control</label>
    <div class="controls">
      <select ng-model="analysisControl" ng-options="selection for selection in selections">
      </select>
    </div>
  </div>
  
  <div class="control-group">
    <label for="" class="control-label">Experiment</label>
    <div class="controls">
      <select ng-model="analysisExperiment" ng-options="selection for selection in selections">
        
      </select>
    </div>
  </div>
  
  <div class="control-group">
    <label for="inputAnalysisValue" class="control-label">p-Value</label>
    <div class="controls">
        <input id="inputAnalysisValue" ng-model="analysisPValue"
        	onblur="if (this.value == '') {this.value = '0.05';}" 
        	onfocus="if (this.value == '0.05') {this.value = '';}" />
    </div>
  </div>
  
</form>

<button class="btn btn-success btn-block" ng-click="limmaInit()" data-dismiss="modal" aria-hidden="true">Analyze</button>
    