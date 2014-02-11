<div class="mev-preset-list-wrapper">
<div class="mev-filter-form-wrapper">
<form class="mev-filter-form"><input ng-model="filterText" placeholder="filter" type="text" value="" id="hi"></input></form>
</div>
<table ng-show="showImport==false" class="mev-table-tight table table-striped" style="font-size:14px;">
<thead>
<tr>
<th><a href="" ng-click="orderByColumn='name';orderByReverse=!orderByReverse" >Dataset</a></th>
<th><a href="" ng-click="orderByColumn='diseaseName';orderByReverse=!orderByReverse" >Disease Name</a></th>
<th><a href="" ng-click="orderByColumn='platform';orderByReverse=!orderByReverse" >Platform Name</a></th>
</tr>
</thead>
<tbody>
<tr ng-repeat="preset in presets | filter:filterText | orderBy:orderByColumn:orderByReverse">
<td><a href="" ng-click="showImportPreset(preset.name)">{{preset.name}}</a></td>
<td>{{preset.diseaseName}}</td>
<td>{{preset.platform}}</td>
</tr>
</tbody>
</table>
<div ng-show="showImport" class="row-fluid" id="mev-iframe-import-presets" >
<iframe  scrolling="no" frameborder="0" width="99%" height="80%" ng-src="{{importPresetUrl}}" />
</div>
</div>