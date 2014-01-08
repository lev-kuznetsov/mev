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
			  
			  <selection-set-manager heatmap-data="heatmapData"></selection-set-manager>
			  
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