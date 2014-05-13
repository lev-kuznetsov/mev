
<bsmodal bindid="columnSelectionsModal" func="" header="Add Column Selections">
	<form role="form">

		<div class="form-group">
		    <label for="columnSelectionName" class="control-label">Name:</label>
		        <input id="columnSelectionName" ng-model="visualization.view.selectionParams.column.name">
		</div>
		
		<button class="btn btn-default" ng-click="clearSelections('column')">Clear Selections</button>
		<button class="btn btn-success" ng-click="addSelection('column')" data-dismiss="modal" aria-hidden="true">Add Selections</button>
		
		
	</form>
</bsmodal> 

<bsmodal bindid="rowSelectionsModal" func="" header="Add Row Selections">
	<form role="form">

		<div class="form-group">
		    <label for="rowSelectionName" class="control-label">Name:</label>
		        <input id="rowSelectionName" ng-model="visualization.view.selectionParams.row.name">
		</div>
		
		
		<button class="btn btn-default" ng-click="clearSelections('row')">Clear Selections</button>
		<button class="btn btn-success" ng-click="addSelection('row')" data-dismiss="modal" aria-hidden="true">Create Selections</button>
		
		
	</form>
</bsmodal> 

<bsmodal bindid="settingsModal" header="Heatmap Visualization Settings">
	<heatmap-Settings current-colors="currentColors"
		available-color-groups="availableColorGroups"
		color-Edge="colorEdge"
		apply-New-Ranges="applyNewRanges"></heatmap-Settings>
</bsmodal>