<div class="container-fluid">
	<form class="form-horizontal" role="form" >
	
		<div class="form-group">
			<label for="heatmapColoring" class="control-label">D3 Color Brewer Groups:</label>
			<select id="heatmapColoring" ng-model="currentColors.group" ng-options="selection for selection in availableColorGroups"></select>
	    </div>
	    
	    
		<button class="btn btn-success btn-block" data-dismiss="modal" aria-hidden="true">Save</button>
		       
	</form>
</div>