

<accordion id="setmanagerAccordion" close-others="true" >
	<accordion-group heading="Column Sets" is-open="isItOpen" ng-init="isItOpen=false">
	
	
		<selection-set-list 
					mev-selections="heatmapData.column.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/column"
					mev-demintion="column">				
					You may define sets using <a href="" ng-click="showAnnotations(selection, 'column')">column annotations</a> or by performing an analysis.
		</selection-set-list>
		<button class="btn btn-success" ng-click="showAnnotations(selection, 'column')" >Add New</button>
		<button ng-show="heatmapData.column.selections.length>0" class="btn btn-primary" data-target="#selectionSetAddColumn" data-toggle="modal">Merge Selected</button>
				<button ng-show="heatmapData.column.selections.length>0" class="btn btn-success pull-right" >
	        <a href="/dataset/{{heatmapId}}/column/selections?format=tsv">
	          <i class="icon-white icon-download"></i> Download
	        </a> 
    	</button>
 
	</accordion-group>

	<accordion-group heading="Row Sets">
		<selection-set-list 
					mev-selections="heatmapData.row.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/row"
					mev-demintion="row">
					You may define sets using <a href="" ng-click="showAnnotations(selection, 'row')">row annotations</a> or by performing an analysis.				
		</selection-set-list>
		<button class="btn btn-success" ng-click="showAnnotations(selection, 'row')" >Add New</button>
		<button ng-show="heatmapData.row.selections.length>0" class="btn btn-primary" data-target="#selectionSetAddRow" data-toggle="modal">Merge Selected</button>		
		<button ng-show="heatmapData.row.selections.length>0" class="btn btn-success pull-right" >
	        <a href="/dataset/{{heatmapId}}/row/selections?format=tsv">
	          <i class="icon-white icon-download"></i> Download
	        </a> 
    	</button>
	</accordion-group>
</accordion>

<bsmodal bindid="selectionSetAddColumn" func="" header="Merge Column Selections">

	<div class="row">
		<form-group>
			<form>
				New Name: <input type="text" class="input-small" ng-model="selectionParams.column.name">
			<form>
		</form-group>
	</div>
	<div class="row">
        <button class="btn btn-success pull-right" >
            <a ng-click="addMergedSelection('column')" data-dismiss="modal" aria-hidden="true">
              Create Selection
            </a> 
        </button>
    </div>
    
</bsmodal> 

<bsmodal bindid="selectionSetAddRow" func="" header="Merge Row Selections">

	<div class="row">
		<form-group>
			<form>
				New Name: <input type="text" class="input-small" ng-model="selectionParams.column.name">
			<form>
		</form-group>
	</div>
	<div class="row">
        <button class="btn btn-success pull-right" >
            <a ng-click="addMergedSelection('row')" data-dismiss="modal" aria-hidden="true">
              Create Selection
            </a> 
        </button>
    </div>
    
</bsmodal> 