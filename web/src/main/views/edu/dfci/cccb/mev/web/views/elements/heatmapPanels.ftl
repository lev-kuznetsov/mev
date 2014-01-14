<br>



<div class="span12">

	<div ng-controller="MainPanelController" class="row-fluid"> <!-- Start Column Expand Tabs -->	
	
	    
		<div class="span3" id="leftPanel">
		
		  
		
			<div class="well" style="height: 100%">
		  
				<side-Navigation-Bar></side-Navigation-Bar>
		  
				<div class="row-fluid">
				    <! -- LEFT EXPANDER BUTTON -->
					<button id="expandLeft" class="btn btn-primary pull-right" ng-click="expandLeft()"><i class="icon-chevron-right"></i></button>
					<button id="closeLeft" class="btn btn-primary pull-right" ng-click="expandBoth()"><i class="icon-chevron-left"></i></button>
				</div>
				
			  
			</div> <!-- End well def -->
		</div>
	
		<div class="span9" id="rightPanel">
		
			<analysis-Menu-Bar></analysis-Menu-Bar>
		
		   <div class="well">
				<div class="row-fluid">
				  <!-- RIGHT EXPANDER BUTTON -->
			    	<button class="btn btn-primary pull-right" id="expandRight" ng-click="expandRight()"><i class="icon-chevron-left"></i></button>
					<button class="btn btn-primary pull-right" id="closeRight" ng-click="expandBoth()"><i class="icon-chevron-right"></i></button>
			    </div>
				
				<br>
				
				<div id="heatmapTabPane" class="tab-pane" expression-Panel></div>
				    
			</div>
			
			
	
		</div>
	</div>
</div>