

<div class="panel-group" id="{{cluster.parentId}}" ng-repeat="cluster in previousHCLClusters">
  <div class="panel panel-default">
    <div class="panel-heading">
      <h4 class="panel-title">
        <a data-toggle="collapse" data-parent="{{cluster.dataParent}}" href="{{cluster.href}}">
          {{cluster.datar.type}} : {{cluster.name}} 
        </a>
      </h4>
    </div>
    <div id="{{cluster.divId}}" class="panel-collapse">
      <div class="panel-body">
      
        	<div class="row">
	          <div class="span12">
	          <button class="btn btn-success pull-right" ng-click="updateHeatmapData(cluster.name, cluster.datar)">
	           Apply to heatmap <i class='icon-chevron-right'></i>
	          </button>
	          </div>
	        </div>
	        <br>
	        <div class="row">
	          <div class="span12">
	            <div d3-Radial-Tree data="cluster.datar" diameter='400'></div> 
	          </div>
	        </div>
	        
      </div>
    </div>
  </div>
</div>
