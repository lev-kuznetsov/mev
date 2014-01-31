<div class="mev-preset-list-wrapper">
<div class="mev-filter-form-wrapper">
<form class="mev-filter-form"><input ng-model="filterText" placeholder="filter" type="text" value="" id="hi"></input></form>
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
<tr ng-repeat="preset in presets | filter:filterText | orderBy:orderByColumn:orderByReverse">
<td><a href="/annotations/import-dataset/command/core/view-preset-annotations?import-preset={{preset.name}}">{{preset.name}}</a></td>
<td>{{preset.diseaseName}}</td>
<td>{{preset.platform}}</td>
</tr>
</tbody>
</table>
</div>