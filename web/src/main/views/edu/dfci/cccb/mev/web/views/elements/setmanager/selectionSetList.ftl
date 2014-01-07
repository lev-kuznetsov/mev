<div class="selectionSetList">
  	<div ng-show="heatmapData.column.selections.length>0" ng-repeat="selection in heatmapData.column.selections" class="selectionSetListItem">
  		<div class="selectionSetColor" style='background-color: {{selection.properties.selectionColor}}'></div>
  		<div><a href="/annotations/{{heatmapId}}/annotation/column/{{selection.name}}/{{selection.properties.selectionFacetLink}}">{{selection.name}}</a></div>
  		<div class="selectionSetDescription">{{selection.properties.selectionDescription}}</div>
  		<div class="selectionSetKeys">
   		<span ng-repeat="key in selection.keys">
   			{{key}},
   		</span>
  		</div>				        		
  	</div>
  	<div ng-show="heatmapData.column.selections.length<=0" class="selectionSetListItem">No sets defined. You may define sets using <a href="/annotations/{{heatmapId}}/annotation/column/new/">column annotations</a> or by performing a clustering analysis.</div>
</div>