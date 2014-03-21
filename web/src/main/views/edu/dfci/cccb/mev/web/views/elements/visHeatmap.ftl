
<bsmodal bindid="columnSelectionsModal" func="" header="Add Column Selections">
	<form role="form">

		<div class="form-group">
		    <label for="columnSelectionName" class="control-label">Name:</label>
		        <input id="columnSelectionName" ng-model="columnSelectionName">
		</div>
		
		<button class="btn btn-success btn-block" ng-click="addTreeSelection('column', 'horizontal', columnSelectionName)" data-dismiss="modal" aria-hidden="true">Add Selections</button>
		
		
	</form>
</bsmodal> 

<bsmodal bindid="rowSelectionsModal" func="" header="Add Row Selections">
	<form role="form">

		<div class="form-group">
		    <label for="rowSelectionName" class="control-label">Name:</label>
		        <input id="rowSelectionName" ng-model="rowSelectionName">
		</div>
		
		<button class="btn btn-success btn-block" ng-click="addTreeSelection('row', 'vertical', rowSelectionName)" data-dismiss="modal" aria-hidden="true">Add Selections</button>
		
		
	</form>
</bsmodal> 