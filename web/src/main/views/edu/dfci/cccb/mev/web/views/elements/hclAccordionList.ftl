
<accordion close-others="false">
	<div ng-repeat="cluster in previousHCLClusters">
    	<accordion-group heading="{{cluster.type}} : {{cluster.name}}" is-open="isItOpen">
      		<div class="row">
	          <div class="span12">
	          <button class="btn btn-success pull-right" ng-click="updateHeatmapTree(cluster)">
	           Apply to heatmap <i class='icon-chevron-right'></i>
	          </button>
	          </div>
	        </div>
	        <br>
	        <div class="row">
	          <div class="span12">
	            <div d3-Radial-Tree data="cluster" diameter='400'></div> 
	          </div>
	        </div>
    	</accordion-group>
    </div>
</accordion>

