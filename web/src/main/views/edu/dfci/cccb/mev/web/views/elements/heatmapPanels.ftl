<div class="row-fluid"> <!-- Start Column Expand Tabs -->
	<div class="span6 marker" id="leftPanel">
		<div class="well">
		
			<div class="row-fluid">
				<button id="expandLeft" class="btn btn-primary pull-right" ng-click="expandLeft()"><i class="icon-chevron-right"></i></button>
				<button id="closeLeft" class="btn btn-primary pull-right" ng-click="expandBoth()"><i class="icon-chevron-left"></i></button>
			</div>
			
			<br>
			
		    <div expression-Panel></div>
		</div>
	</div>

	<div class="span6 marker" id="rightPanel">
		<div class="well">
			<div class="row-fluid">
		    	<button class="btn btn-primary pull-right" id="expandRight" ng-click="expandRight()"><i class="icon-chevron-left"></i></button>
				<button class="btn btn-primary pull-right" id="closeRight" ng-click="expandBoth()"><i class="icon-chevron-right"></i></button>
		    </div>
			<div class="row-fluid">
                <p class="lead pull-left">Set Manager</p>
		    </div>
			<div class="row-fluid">	
			  <div class="accordion">
			    <div class="accordion-group">
			    
				    <div class="accordion-heading">
				      <a class="accordion-toggle" data-toggle="collapse" data-parent="" href="#collapseSetManagerColumns">
				        Column Sets
				      </a>
				    </div> <!-- End Heading Div -->
				    
				    <div id="collapseSetManagerColumns" class="accordion-body collapse" style="height: auto;">
				      <div class="accordion-inner">
				        <div class="table">
				        	<div ng-repeat="selection in theData.column.selections" >
				        		<div style='margin-right: .3em; display: inline; width: 1em; height: 1em; float: left; background-color: {{selection.properties.selectionColor}}'></div>
				        		<div><a href="/annotations/{{heatmapId}}/annotation/0/{{selection.properties.selectionFacetLink}}">{{selection.name}}</a></div>
				        		<div>{{selection.properties.selectionDescription}}</div>
				        		<div style='overflow: hidden; text-overflow: ellipsis; -o-text-overflow: ellipsis; white-space: nowrap;'>
					        		<span ng-repeat="key in selection.keys">
					        			{{key}},
					        		</span>
				        		</div>				        		
				        	</div>
				        </table>
				      </div>
				    </div>
			    
			    </div> <!-- End Accordion Grouping -->
			  </div> <!-- End Accordion Definition -->
			
			</div> <!-- End Row Definition -->
					
					
			<div class="row-fluid">
                <p class="lead pull-left">Analysis</p>
		    </div>
		    <hr>
		    
		    <div analysis-Panel></div>
	    </div>
	</div>
</div> <!--End column expand tabs -->