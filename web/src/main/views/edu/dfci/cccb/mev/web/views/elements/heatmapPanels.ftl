<style>
/* by default, remove left-hand margin on all spans of class .marker */

.row-fluid > [class*="span"].marker{  
  margin-left:0;
  
}


/* restore default left hand margin on all but the first span of class .marker */

.row-fluid > [class*="span"].marker ~ [class*="span"].marker{
  margin-left: 0%;
  margin-left: 2.127659574468085%;
}

/* since this is a responsive example, tweak the left margin at different viewports */

@media (max-width: 979px) and (min-width: 768px){
    .row-fluid > [class*="span"].marker ~ [class*="span"].marker{
    margin-left: 2.7624309392265194%;
}
}

@media (min-width: 1200px){
    .row-fluid > [class*="span"].marker ~ [class*="span"].marker{
    margin-left:2.564102564102564%;
}
}


@media (max-width: 767px){
    .row-fluid > [class*="span"].marker ~ [class*="span"].marker{
    margin-left:0;
}

}

</style>

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
		    	<button class="btn btn-primary pull-left" id="expandRight" ng-click="expandRight()"><i class="icon-chevron-left"></i></button>
				<button class="btn btn-primary pull-left" id="closeRight" ng-click="expandBoth()"><i class="icon-chevron-right"></i></button>
		    </div>
		    
		    <br>
		    
		    <div analysis-Panel></div>
	    </div>
	</div>
</div> <!--End column expand tabs -->