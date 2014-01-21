
<br>

<div class="row-fluid"=>
	<div class="span10 offset1">
		<p class="lead">Selections</p>
		<hr>
		<selection-set-manager id="selectionSetMgr" ng-controller="SelectionSetManagerCtl" heatmap-data="heatmapData" heatmap-id="{{heatmapId}}"></selection-set-manager>
	</div>
</div>

<div class="row-fluid"=>
	<div class="span10 offset1">
		<p class="lead">Previous Analysis</p>
		<hr>
		<ul>
			<li>Clusters </li>
			<ul>
				<li ng-repeat="analysis in previousHCLClusters">
				
					<a href="#clusters-tab" data-toggle="tab">{{analysis.name}}</a>
					
				</li>
			</ul>
			<li>LIMMA </li>
			<ul>
				<li ng-repeat="analysis in previousLimmaAnalysis">
					<a href="#limma-tab" data-toggle="tab">{{analysis.name}} </a>
				</li>
			</ul>
		</ul>
	</div>
</div>