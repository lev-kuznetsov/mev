

<accordion id="setmanagerAccordion" close-others="false" >
	<accordion-group heading="Column Sets" is-open="isItOpen" ng-init="isItOpen=true">
	
	
		<selection-set-list 
					mev-selections="heatmapData.column.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/column"
					mev-demintion="column">				
					You may define sets using <a href="" ng-click="showAnnotations(selection, 'column')">column annotations</a> or by performing a clustering analysis.				
		</selection-set-list>
	</accordion-group>

	<accordion-group heading="Row Sets">
		<selection-set-list 
					mev-selections="heatmapData.row.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/row"
					mev-demintion="row">
					You may define sets uploading your <a href="" ng-click="showAnnotations(selection, 'row')">row annotations</a> or by looking up <a href="" ng-click="showAnnotations(selection, 'row', 'probe')">probe annotations</a> from our daaccordion-groupase.				
		</selection-set-list>
	</accordion-group>
</accordion>