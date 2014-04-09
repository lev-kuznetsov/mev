<div>
<div class="selectionSetList clearfix">	
	<div ng-show="selections.length>0" ng-repeat="selection in selections"
		class="selectionSetListItem">
<!-- 		<a data-toggle="modal" role="button" data-target="#editModal" ng-click="setSelected(selection)"> -->
			<div class="selectionSetColor"
				style='background-color: {{selection.properties.selectionColor}}'></div>
<!-- 		</a> -->
		<div class="selectionSetName">
			<!-- href="{{baseUrl}}/{{selection.name}}/{{selection.properties.selectionFacetLink}} -->
			<a href=""
				ng-click="showAnnotations(selection, dimension)">{{selection.name}}</a>
		
			<input type="checkbox" ng-model="selection.setSelectionChecked" ng-true-value="true" ng-false-value="false">
		</div>
		<div class="selectionSetDescription">{{selection.properties.selectionDescription}}</div>
		<div class="selectionSetKeys">
			<span ng-repeat="key in selection.keys.slice(0, 3)"> {{key}}, </span> ...
		</div>		
	</div>
	<div ng-show="selections.length<=0" class="selectionSetListItem">	
		<span ng-transclude>No sets defined.</span>
	</div>
</div>


	<bsmodal bindid="#editModal" func="" header="Edit {{selectedItem.name}}">
		<selection-set-edit-form></selection-set-edit-form>		
	</bsmodal>
	
</div>
