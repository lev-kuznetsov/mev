
<br>


<div class="fixed-height">
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
			
			<cluster-Accordion-List></cluster-Accordion-List>
				
			<limma-Accordion-List></limma-Accordion-List>
	
				
			
		</div>
	</div>
<div>