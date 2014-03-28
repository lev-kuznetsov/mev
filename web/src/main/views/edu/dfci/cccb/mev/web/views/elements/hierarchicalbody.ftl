      <form role="form">

          <div class="form-group">
            <label for="hclClusterName" class="control-label">Cluster Name</label>

            <input id="hclClusterName" ng-model="params.name" placeholder="Ex: My_Analysis_1">
          </div>
          
          <div class="form-group">
            <label for="hclClusterMetric" class="control-label">Distance Metric</label>

            <select id="hclClusterMetric" ng-options="option.name for option in options.metrics" ng-model="params.metric"></select>
          </div>
          
          <div class="form-group">
            <label for="hclClusterAlgorithm" class="control-label">Linkage Criteria Algorithm</label>

            <select id="hclClusterAlgorithm" ng-options="option.name for option in options.linkage" ng-model="params.linkage"></select>
          </div>

          <div class="form-group">
            <label for="hclClusterDimension" class="control-label">Clustering Dimension</label>

            <select id="hclClusterDimension" ng-options="option.name for option in options.dimensions" ng-model="params.dimension"></select>
          </div>
          
          
          
     </form> 
     		
     <button class="btn btn-success btn-block" ng-click="clusterInit()" data-dismiss="modal" aria-hidden="true">Analyze</button>
    		
