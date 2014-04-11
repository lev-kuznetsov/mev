<div class="mev-preset-list-wrapper">
<!-- 
<div class="mev-filter-form-wrapper" >
	<form class="mev-filter-form form-inline" role="form"">
		<div class="form-group">
			<label class="sr-only" for="filterText">filter</label>
			<input id="filterText" class="form-control input-sm" ng-model="filter.text" placeholder="filter" type="text" value="" />
		</div>
		<label class="radio-inline" ng-repeat="level in data.levels">
		  {{level}}<input  id="filterText{{$index}}" type="radio" value="{{level}}" ng-model="filter.level" ng-change="filter.text=''">
		</label>	
	</form>
</div>

<table class="mev-table-tight table table-striped" style="font-size:14px;">
<thead>
<tr>
<th><a href="" ng-click="orderByColumn='name';orderByReverse=!orderByReverse" >Dataset</a></th>
<th><a href="" ng-click="orderByColumn='diseaseName';orderByReverse=!orderByReverse" >Disease Name</a></th>
<th><a href="" ng-click="orderByColumn='platform';orderByReverse=!orderByReverse" >Platform Name</a></th>
</tr>
</thead>
<tbody>
<tr ng-repeat="preset in presets | filter:filter.text | filter:filter.level | orderBy:orderByColumn:orderByReverse">
<td><a href="" ng-click="showImportPreset(preset.name)">{{preset.name}}</a></td>
<td>{{preset.diseaseName}}</td>
<td>{{preset.platform}}</td>
</tr>
</tbody>
</table>


<div ng-show="showImport" class="row-fluid" id="mev-iframe-import-presets" >

<iframe  scrolling="no" frameborder="0" width="99%" height="80%" ng-src="{{importPresetUrl}}" />

</div>
 -->
<div ng-show="showImport==false" id="presetsGrid" class="gridStyle" ng-grid="gridOptions" ></div>
</div>

<bsmodal id="import-presets-dialog" bindid="import-presets-modal" func="" header="Import Tcga Presets">
<iframe  scrolling="no" frameborder="0" width="99%" height="80%" ng-src="{{importPresetUrl}}" /> 
</bsmodal>