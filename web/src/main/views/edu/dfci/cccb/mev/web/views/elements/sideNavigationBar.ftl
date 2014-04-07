
<div class="fixed-height">

	<div class="col-md-10 col-md-offset-1">
		<br>
		<div class="row"=>
			<selection-set-manager id="selectionSetMgr" ng-controller="SelectionSetManagerCtl" heatmap-data="heatmapData" heatmap-id="{{heatmapId}}"></selection-set-manager>
		</div>

		<div class="row"=>
		<hr>
			<hcl-Accordion-List></hcl-Accordion-List>
				
			<limma-Accordion-List></limma-Accordion-List>
			
			<kmeans-Accordion-List></kmeans-Accordion-List>
			
			<t-Test-Accordion-List></t-Test-Accordion-List>
		</div>
		
	</div>
	
<div>