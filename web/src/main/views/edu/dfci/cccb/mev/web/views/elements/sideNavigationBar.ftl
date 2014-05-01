
<div class="fixed-height">

	<div class="col-md-10 col-md-offset-1">
		<br>
		<div class="row">
			<selection-set-manager id="selectionSetMgr" ng-controller="SelectionSetManagerCtl" heatmap-data="heatmapData" heatmap-id="{{heatmapId}}"></selection-set-manager>
		</div>

		<div class="row">
		<hr>
		
		<accordion close-others="false">
			<div ng-repeat="analysis in dataset.analyses track by $index">
				<analysis-Content-Item analysis="analysis" heatmap-Dataset="dataset">
					</analysis-Content-Item>
			</div>
		</accordion>
			


		</div>
		
	</div>
	
<div>