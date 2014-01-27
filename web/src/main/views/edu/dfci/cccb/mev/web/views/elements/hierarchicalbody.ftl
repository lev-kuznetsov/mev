      <form class="form-horizontal">

          <div class="control-group">
            <label for="clusterName" class="control-label">Cluster Name</label>
            <div class="controls">
              <input id="hclClusterName" ng-model="clusterName" placeholder="Ex: My_Analysis_1">
            </div>
          </div>
          
          <div class="control-group">
            <label for="metricName" class="control-label">Distance Metric</label>
            <div class="controls">
              <select id="hclClusterMetric" ng-options="option for option in availableMetrics" ng-model="selectedMetric"></select>
            </div>
          </div>
          
          <div class="control-group">
            <label for="algorithmName" class="control-label">Linkage Criteria Algorithm</label>
            <div class="controls">
              <select id="hclClusterAlgorithm" ng-options="option for option in availableAlgorithms" ng-model="selectedAlgorithm"></select>
            </div>
          </div>

          <div class="control-group">
            <label for="dimension" class="control-label">Clustering Dimension</label>
            <div class="controls">
              <select id="hclClusterDimension" ng-options="option.name for option in dimensions" ng-model="selectedDimension"></select>
            </div>
          </div>
          
          
          
     </form> 
     		
     <button class="btn btn-success btn-block" ng-click="clusterInit()" data-dismiss="modal" aria-hidden="true">Analyze</button>
    		
