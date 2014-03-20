
<div class="fixed-height">

	<div class="col-md-10 col-md-offset-1">
		<div class="row"=>
			<div class="sidepanel-title row">
				<p class="pull-right">Selections</p>
			</div>
	
			<selection-set-manager id="selectionSetMgr" ng-controller="SelectionSetManagerCtl" heatmap-data="heatmapData" heatmap-id="{{heatmapId}}"></selection-set-manager>
		</div>
	
	
		<div class="row"=>
		<hr>
			
			<cluster-Accordion-List></cluster-Accordion-List>
				
			<limma-Accordion-List></limma-Accordion-List>
		</div>
		
	</div>
	
<div>