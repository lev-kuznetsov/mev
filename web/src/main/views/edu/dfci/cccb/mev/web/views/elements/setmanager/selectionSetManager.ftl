<accordion id="setmanagerAccordion" close-others="true" >
	<accordion-group heading="Column Sets" is-open="isItOpen" ng-init="isItOpen=false">
	
		<div class="btn-group pull-right">
		  <button class="btn btn-sm btn-info dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
		    Set Options
		    <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
		  	<li role="presentation"><a role="menuitem" tabindex="-1" data-target="#selectionSetExportDatasetColumn" 
				data-toggle="modal">Export as New Dataset</a></li>			
		    <li role="presentation"><a role="menuitem" tabindex="-1" data-target="#selectionSetAddColumn" 
				data-toggle="modal">Merge Selected</a></li>
		    <li role="presentation"><a role="menuitem" tabindex="-1" data-target="#selectionSetIntColumn" 
				data-toggle="modal">Intersect Selected</a></li>
		    <li role="presentation"><a role="menuitem" tabindex="-1" data-target="#selectionSetDiffColumn" 
				data-toggle="modal">Difference Selected</a></li>
		  </ul>
		</div>
<!-- 		<input type="button" ng-click="sayHelloCtl()" value="hi" /> -->
		<selection-set-list 
					mev-selections="heatmapData.column.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/column"
					mev-demintion="column">				
					You may define sets using <a href="" ng-click="showAnnotations(selection, 'column')">column annotations</a> or by performing an analysis.
		</selection-set-list>
		<button class="btn btn-success" ng-click="showAnnotations(selection, 'column')" >Add New</button>
		
			
		<a ng-show="heatmapData.column.selections.length>0" class="btn btn-success pull-right" href="/dataset/{{heatmapId}}/column/selections?format=tsv">
	          <i class="icon-white icon-download"></i> Download
	        </a>
 
	</accordion-group>

	<accordion-group heading="Row Sets">
	
		<div class="btn-group pull-right">
		  <button class="btn btn-sm btn-info dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown">
		    Set Options
		    <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu2">
		  	<li role="presentation"><a role="menuitem" tabindex="-1" data-target="#selectionSetExportDatasetRow" 
				data-toggle="modal">Export as New Dataset</a></li>	
		    <li role="presentation"><a role="menuitem" tabindex="-1" data-target="#selectionSetAddRow" 
				data-toggle="modal">Merge Selected</a></li>
		    <li role="presentation"><a role="menuitem" tabindex="-1" data-target="#selectionSetIntRow" 
				data-toggle="modal">Intersect Selected</a></li>
		    <li role="presentation"><a role="menuitem" tabindex="-1" data-target="#selectionSetDiffRow" 
				data-toggle="modal">Difference Selected</a></li>			
		  </ul>
		</div>
		
		<selection-set-list 
					mev-selections="heatmapData.row.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/row"
					mev-demintion="row">
					You may define sets using <a href="" ng-click="showAnnotations(selection, 'row')">row annotations</a> or by performing an analysis.				
		</selection-set-list>
		<button class="btn btn-success" ng-click="showAnnotations(selection, 'row')" >Add New</button>
				
		<a ng-show="heatmapData.row.selections.length>0" class="btn btn-success pull-right" href="/dataset/{{heatmapId}}/row/selections?format=tsv">
	          <i class="icon-white icon-download"></i> Download
	        </a>
	        
	</accordion-group>
</accordion>

<bsmodal bindid="selectionSetAddColumn" func="" header="Merge Column Selections">

	<div class="row">
		<form-group>
			<form>
				<label for="uniColName" class="control-label">New Name:</label>
				<input id="uniColName" type="text" class="input-small" ng-model="selectionParams.column.name">
			<form>
		</form-group>
	</div>
	<div class="row">
        <a class="btn btn-success pull-right" ng-click="addMergedSelection('column')" data-dismiss="modal" aria-hidden="true">
              Create Selection
        </a>
    </div>
    
</bsmodal> 



<bsmodal bindid="selectionSetAddRow" func="" header="Merge Row Selections">

	<div class="row">
		<form-group>
			<form>
				<label for="uniRowName" class="control-label">New Name:</label>
				<input id="uniRowName" type="text" class="input-small" ng-model="selectionParams.row.name">
			</form>
		</form-group>
	</div>
	<div class="row">
        <a class="btn btn-success pull-right" ng-click="addMergedSelection('row')" data-dismiss="modal" aria-hidden="true">
              Create Selection
        </a>
    </div>
    
</bsmodal> 

<bsmodal bindid="selectionSetExportDatasetColumn" func="" header="Export as New Dataset">

	<div class="row">
		<form-group>
			<form>
				<label for="uniColName" class="control-label">New Dataset Name:</label>
				<input id="uniColName" type="text" class="input-small" ng-model="exportParams.column.name">
			<form>
		</form-group>
	</div>
	<div class="row">
        <a class="btn btn-success pull-right" ng-click="exportSelection('column')" data-dismiss="modal" aria-hidden="true">
              Export as New Dataset
        </a>
    </div>
    
</bsmodal> 


<bsmodal bindid="selectionSetExportDatasetRow" func="" header="Export as New Dataset">

	<div class="row">
		<form-group>
			<form>
				<label for="uniRowName" class="control-label">New Dataset Name:</label>
				<input id="uniRowName" type="text" class="input-small" ng-model="exportParams.row.name">
			</form>
		</form-group>
	</div>
	<div class="row">
        <a class="btn btn-success pull-right" ng-click="exportSelection('row')" data-dismiss="modal" aria-hidden="true">
              Export as New Dataset
        </a>
    </div>
    
</bsmodal> 

<!-- Intersections -->

<bsmodal bindid="selectionSetIntColumn" func="" header="Intersect Column Selections">

	<div class="row">
		<form-group>
			<form>
				<label for="intColName" class="control-label">New Name:</label>
				<input id="intColName" type="text" class="input-small" ng-model="selectionParams.column.name">
			</form>
		</form-group>
	</div>
	
	<div class="row">
        <a class="btn btn-success pull-right" ng-click="addIntersectionSelection('column')" data-dismiss="modal" aria-hidden="true">
              Create Selection
        </a>
    </div>
    
</bsmodal> 

<bsmodal bindid="selectionSetIntRow" func="" header="Intersect Row Selections">

	<div class="row">
		<form-group>
			<form>
				<label for="intRowName" class="control-label">New Name:</label>
				<input id="intRowName" type="text" class="input-small" ng-model="selectionParams.row.name">
			</form>
		</form-group>
	</div>
	<div class="row">
        <a class="btn btn-success pull-right" ng-click="addIntersectionSelection('row')" data-dismiss="modal" aria-hidden="true">
              Create Selection
        </a>
    </div>
    
</bsmodal>

<!-- Set Differences -->

<bsmodal bindid="selectionSetDiffColumn" func="" header="Set Difference Column Selections">

	<div class="row">
		<form-group>
			<form>
				<label for="diffColName" class="control-label">New Name:</label>
				<input id="diffColName" type="text" class="input-small" ng-model="selectionParams.column.name">
			</form>
		</form-group>
	</div>
	
	<div class="row">
		<form-group>
			<form>
				<label for="baseColSetSelector" class="control-label">Base Set:</label>
				<select id="baseColSetSelector" ng-options="group.name for group in heatmapData.column.selections" 
							ng-model="selectionParams.special.column"></select>
			</form>
		</form-group>
	</div>
	
	<div class="row">
        <a class="btn btn-success pull-right" ng-click="addDifferenceSelection('column')" data-dismiss="modal" aria-hidden="true">
              Create Selection
        </a>
    </div>
    
</bsmodal> 

<bsmodal bindid="selectionSetDiffRow" func="" header="Set Difference Row Selections">

	<div class="row">
		<form-group>
			<form>
				<label for="diffRowName" class="control-label">New Name: </label>
				<input id="diffRowName" type="text" class="input-small" ng-model="selectionParams.row.name">
			<form>
		</form-group>
	</div>
	
	<div class="row">
		<form-group>
			<form>
				<label for="baseRowSetSelector" class="control-label">Base Set:</label>
				<select id="baseRowSetSelector" ng-options="group.name for group in heatmapData.row.selections" 
							ng-model="selectionParams.special.row"></select>
			</form>
		</form-group>
	</div>
	
	<div class="row">
        <a class="btn btn-success pull-right" ng-click="addDifferenceSelection('row')" data-dismiss="modal" aria-hidden="true">
              Create Selection
        </a> 
    </div>
    
</bsmodal>

