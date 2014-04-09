

<accordion id="setmanagerAccordion" close-others="true" >
	<accordion-group heading="Column Sets" is-open="isItOpen" ng-init="isItOpen=false">
	
	
		<selection-set-list 
					mev-selections="heatmapData.column.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/column"
					mev-demintion="column">				
					You may define sets using <a href="" ng-click="showAnnotations(selection, 'column')">column annotations</a> or by performing an analysis.				
		</selection-set-list>
		<button class="btn btn-success" ng-click="showAnnotations(selection, 'column')" >Add New</button>
	</accordion-group>

	<accordion-group heading="Row Sets">
		<selection-set-list 
					mev-selections="heatmapData.row.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/row"
					mev-demintion="row">
					You may define sets using <a href="" ng-click="showAnnotations(selection, 'row')">row annotations</a> or by performing an analysis.				
		</selection-set-list>
		<button class="btn btn-success" ng-click="showAnnotations(selection, 'row')" >Add New</button>
	</accordion-group>
</accordion>