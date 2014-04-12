
<accordion close-others="false">
	<div ng-repeat="cluster in previousHCLClusters">
    	<accordion-group heading="{{cluster.type}} : {{cluster.name}}" is-open="isItOpen">
      		<div class="row">
	          <div class="span12">
	          <button class="btn btn-success pull-left" ng-click="saveImage(cluster)">
	           Save Image
	          </button>
	          <button class="btn btn-success pull-right" ng-click="updateHeatmapView(cluster)">
	           Apply to heatmap <i class='icon-chevron-right'></i>
	          </button>
	          </div>
	        </div>
	        <br>
	        <div class="row">
	          <div class="span12">
	            <div id="svgWrapperHclTree_{{cluster.name}}" d3-Radial-Tree data="cluster" diameter='400'></div> 
	          </div>
	        </div>
<!-- 	        <div id="svgdataurl"></div> -->
<!-- 	        <div id="pngdataurl"></div> -->
	        <canvas id="canvasHclTree_{{cluster.name}}" style="display: none"></canvas>	        
    	</accordion-group>
    </div>
</accordion>

