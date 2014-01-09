<div class="selectionSetList">
	<div ng-show="selections.length>0"
		ng-repeat="selection in selections"
		class="selectionSetListItem">	
		<a data-toggle="modal" role="button" href="#editSetModal">	
		<div  ng-dblclick="sayHello()" class="selectionSetColor" style='background-color: {{selection.properties.selectionColor}}'></div>
		</a>
		<div>
			<a href="{{baseUrl}}/{{selection.name}}/{{selection.properties.selectionFacetLink}}">{{selection.name}}</a>
		</div>						
		<div class="selectionSetDescription">{{selection.properties.selectionDescription}}</div>
		<div class="selectionSetKeys">
			<span ng-repeat="key in selection.keys"> {{key}}, </span>
		</div>
	</div>
	<div ng-show="selections.length<=0" class="selectionSetListItem">
		<span ng-transclude>No sets defined.</span> 		
	</div>		
</div> 