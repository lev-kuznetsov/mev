<div class="container">
	<div class="row">
		
		<div class="span7">
		    <vis-Heatmap 
				inputdata="transformeddata"
				inputcolor="red"
				showlabels="true"
				width="700"
				height="700"
				marginleft="80"
				marginright="80"
				margintop="80"
				marginbottom="80"
				celllink="selectedcells"
				pushtomarked="markRow(inputindecies, inputdimension)">
			</vis-Heatmap>
		</div>
		
		<!--End Visualization Column -->
		<div class="span3 offset1">
			
		    <div class="row">
		    	<p>
					<button class="btn btn-mini" type="button" ng-click="pageLeft()"><i class="icon-chevron-left"></i></button>
					<button class="btn btn-mini" type="button" ng-click="pageRight()"><i class="icon-chevron-right"></i></button>
				</p>
		    </div>
		    <div class="row">
		    	<p>
					<button class="btn btn-mini" type="button" ng-click="pageUp()"><i class="icon-chevron-up"></i></button>
					<button class="btn btn-mini" type="button" ng-click="pageDown()"><i class="icon-chevron-down"></i></button>
				</p>
		    </div>
		    <div class="row" >
		 	   <vis-Ranged-Bar 
					inputdata="selectedcells"
				</vis-Ranged-Bar>
		    </div>
		    
		    <div class="row">
				
				<div class="row">
					<input id="file" type="file" multiple name="upload" />
				</div>
				
				<div class="row">
					<div class="progress progress-striped active" id="progbox" style="visibility: hidden;">
						<div class="bar" id="progbar"></div>
						
					</div>
				</div>
				
				<div class="row">
					<button class="btn btn-small btn-primary" ng-click="sendRowFile()">Submit Row Annotations</button>
					<div id="rowoutput"></div>
				</div>
				
				<div class="row">
					<button class="btn btn-small btn-primary" ng-click="sendColFile()">Submit Column Annotations</button>
					<div id="coloutput"></div>
				</div>
				
			</div>
			
			<div class="row">
				<button class="btn btn-block btn-primary" data-target="#myRowModal" data-toggle="modal">Add Row Selection </button>
				<button class="btn btn-block btn-primary" data-target="#myColumnModal" data-toggle="modal">Add Column Selection </button>
			</div>
						
		</div>
		<!--End Column 2 -->
	</div>

</div>

<div id="myRowModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove"></i></button>
    <h3 id="myModalLabel">Add New Filter</h3>
  </div>
  
  <div class="modal-body">
  
		<p>Name:</p><input ng-model='selectionname' />

  </div>
  
  <div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
    <button class="btn btn-primary"  data-dismiss="modal" aria-hidden="true" ng-click="pushSelections(selectionname, 'row') ">Add Row Selection</button>
  </div>
  
</div>

<div id="myColumnModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove"></i></button>
    <h3 id="myModalLabel">Add New Filter</h3>
  </div>
  
  <div class="modal-body">
  
		<p>Name:</p><input ng-model='selectionname' />

  </div>
  
  <div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
    <button class="btn btn-primary"  data-dismiss="modal" aria-hidden="true" ng-click="pushSelections(selectionname, 'column')">Add Column Selection</button>
  </div>
  
</div>
