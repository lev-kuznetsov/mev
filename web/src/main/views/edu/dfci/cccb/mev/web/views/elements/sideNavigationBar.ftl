
<br>



<div class="row-fluid"=>

	
	<div class="span10 offset1">
	
		<div class="sidepanel-title">
			<p class="lead">Selections</p>
		</div>

		<selection-set-manager id="selectionSetMgr" ng-controller="SelectionSetManagerCtl" heatmap-data="heatmapData" heatmap-id="{{heatmapId}}"></selection-set-manager>
	</div>
</div>

<div class="row-fluid"=>
	<div class="span10 offset1">
	
		<div class="sidepanel-title">
			<p class="lead">Previous Analysis</p>
		</div>
		
			Clusters 
			
				
			<div ng-repeat="analysis in previousHCLClusters">
				<div class="row-fluid">
					<div id="prevElement">
					
						<a ng-click="clusterAnalysisClickOpen(analysis)">{{analysis.name}}</a>
					
					</div>
				</div>
			</div>
			
			LIMMA
			
			
			<div ng-repeat="analysis in previousLimmaAnalysis">
				<div class="row-fluid">
					<div id="prevElement">
						<a ng-click="limmaAnalysisClickOpen({{analysis}})">{{analysis.name}} </a>
					</div>
				</div>
			</div>
			
		
	</div>
</div>