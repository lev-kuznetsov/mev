<span></span>

<div class="accordion" id="{{cluster.parentId}}" ng-repeat="cluster in previousHCLClusters">
    <div class="accordion-group">
    
	    <div class="accordion-heading">
	      <a class="accordion-toggle" data-toggle="collapse" data-parent="{{cluster.dataParent}}" href="{{cluster.href}}">
	        {{cluster.datar.type}} : {{cluster.name}} 
	      </a>
	    </div> <!-- End Heading Div -->
	    
	    <div id="{{cluster.divId}}" class="accordion-body collapse">
	      <div class="accordion-inner">
	      
	        <div class="row-fluid">
	          <div class="span12">
	          <button class="btn btn-success pull-right" ng-click="updateHeatmapData(cluster.name, cluster.datar)">
	           Apply to heatmap <i class='icon-chevron-right'></i>
	          </button>
	          </div>
	        </div>
	        <br>
	        <div class="row-fluid">
	          <div class="span12">
	            <div d3-Radial-Tree data="cluster.datar" diameter='400'></div> 
	          </div>
	        </div>
	        
	    </div> <!-- End Body Div -->
    
    </div> <!-- End Accordion Grouping -->
</div> <!-- End Accordion Definition -->

<br>