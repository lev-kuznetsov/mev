<style type="text/css">

.fixed-height-analysis {
  overflow:auto;
  
}

</style>

<div class="row-fluid"> <!-- Start Column Expand Tabs -->
	<div class="span3 marker" id="leftPanel">
	
	  <div class="well">
	  
	     <div class="row-fluid">
			    <! -- LEFT EXPANDER BUTTON -->
				<button id="expandLeft" class="btn btn-primary pull-right" ng-click="expandLeft()"><i class="icon-chevron-right"></i></button>
				<button id="closeLeft" class="btn btn-primary pull-right" ng-click="expandBoth()"><i class="icon-chevron-left"></i></button>
	     </div>
			
	     <div class="fixed-height-analysis">
	        
			
			<div class="row-fluid">
                <p class="lead pull-left">Set Manager</p>
		    </div>
			<div class="row-fluid">
			  
			  <selectionListHeader></selectionListHeader>
			  	
			  <div id="setmanagerAccordion" class="accordion">

				<div class="accordion-group">
			    
				    <div class="accordion-heading">
				      <a class="accordion-toggle" data-toggle="collapse" href="#collapseSetManagerColumns">
				        Column Sets
				      </a>
				      <a class="accordion-action-button" href="/annotations/{{heatmapId}}/annotation/column/new/">create new from annotations</a>
				    </div> <!-- End Heading Div -->
				    
				    <div id="collapseSetManagerColumns" class="accordion-body collapse" >
				      <div class="accordion-inner">
				        <div class="selectionSetList">
				        	<div ng-show="heatmapData.column.selections.length>0" ng-repeat="selection in heatmapData.column.selections" class="selectionSetListItem">
				        		<div class="selectionSetColor" style='background-color: {{selection.properties.selectionColor}}'></div>
				        		<div><a href="/annotations/{{heatmapId}}/annotation/column/{{selection.name}}/{{selection.properties.selectionFacetLink}}">{{selection.name}}</a></div>
				        		<div class="selectionSetDescription">{{selection.properties.selectionDescription}}</div>
				        		<div class="selectionSetKeys">
					        		<span ng-repeat="key in selection.keys">
					        			{{key}},
					        		</span>
				        		</div>				        		
				        	</div>
				        	<div ng-show="heatmapData.column.selections.length<=0" class="selectionSetListItem">No sets defined. You may define sets using <a href="/annotations/{{heatmapId}}/annotation/column/new/">column annotations</a> or by performing a clustering analysis.</div>
				      </div>
				    </div>
			      </div> <!-- End Accordion Body -->
			    </div> <!-- End Accordion Grouping -->
			    			    
			    <div class="accordion-group">
			    
				    <div class="accordion-heading">
				      <a class="accordion-toggle" data-toggle="collapse" href="#collapseSetManagerRows">
				        Row Sets
				      </a>				      
				    </div> <!-- End Heading Div -->
				    
				    <div id="collapseSetManagerRows" class="accordion-body collapse" >
				      <div class="accordion-inner">
				        <div class="selectionSetList">
				        	<div ng-show="heatmapData.row.selections.length>0" ng-repeat="selection in heatmapData.row.selections" class="selectionSetListItem">
				        		<div class="selectionSetColor" style='background-color: {{selection.properties.selectionColor}}'></div>
				        		<div><a href="/annotations/{{heatmapId}}/annotation/row/{{selection.properties.selectionFacetLink}}&{{selection.name}}&{{selection.properties.selectionColor}}&{{selection.properties.selectionDescription}}">{{selection.name}}</a></div>
				        		<div class="selectionSetDescription">{{selection.properties.selectionDescription}}</div>
				        		<div class="selectionSetKeys">
					        		<span ng-repeat="key in selection.keys">
					        			{{key}},
					        		</span>
				        		</div>				        		
				        	</div>
				        	<div ng-show="heatmapData.row.selections.length<=0" class="selectionSetListItem">No sets defined. You may define sets by performing a clustering analysis.</div>
				        </div>
				      </div>
				    </div> <!-- End Accordion Body -->
			    
			    </div> <!-- End Accordion Grouping -->
			    
			  </div> <!-- End Accordion Definition -->
			
			</div> <!-- End Row Definition -->
					
					
			<div class="row-fluid">
                <p class="lead pull-left">Analysis</p>
		    </div>
		    
		    <div analysis-Panel></div>
		    
		  </div> <!-- End fixed-height def -->
	    </div> <!-- End well def -->
	    
		
	</div>

	<div class="span9 marker" id="rightPanel">
	
	   <div class="well">
		
			
			
			<div class="row-fluid">
			  <!-- RIGHT EXPANDER BUTTON -->
		    	<button class="btn btn-primary pull-right" id="expandRight" ng-click="expandRight()"><i class="icon-chevron-left"></i></button>
				<button class="btn btn-primary pull-right" id="closeRight" ng-click="expandBoth()"><i class="icon-chevron-right"></i></button>
		    </div>
			
			<br>
			
		    <div expression-Panel></div>
		</div>
		
		
	</div>
</div> <!--End column expand tabs -->