
<div class="fixed-height">

	<div class="col-md-10 col-md-offset-1">
		<br>
		<div class="row">
		
			<accordion close-others="false">
				<accordion-group heading="View Manager" is-open="isOpen" ng-init="isOpen=true">
				
					<view-Manager project="project"></view-Manager>
				</accordion-group>
			</accordion>
		
		<hr>

		
			<selection-set-manager id="selectionSetMgr" ng-controller="SelectionSetManagerCtl" selections="project.dataset.selections" heatmap-data="project.dataset" heatmap-id="{{project.dataset.datasetName}}"></selection-set-manager>
		<hr>

			<accordion close-others="false">
				<div ng-repeat="analysis in project.dataset.analyses track by $index">
					<analysis-Content-Item analysis="analysis" project="project" generate-View="project.generateView">
						</analysis-Content-Item>
				</div>
			</accordion>
			
		</div>
		
	</div>
	
<div>