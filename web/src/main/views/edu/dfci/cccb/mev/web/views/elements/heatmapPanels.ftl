<span heatmap-Navigation></span

<br>

<div ng-controller="MainPanelController" class="row-fluid"> <!-- Start Column Expand Tabs -->	
	<div class="row-fluid">
	    
		<div id="leftPanel">
				<side-Navigation-Bar></side-Navigation-Bar>
		</div>
		
		<div id="partition">
			<div id='tab'>
				<i class="icon-chevron-left icon-white"></i>
			</div>
		</div>
		
		
		<div id="rightPanel">
				
	    		<analysis-Menu-Bar></analysis-Menu-Bar>
				<div id="heatmapTabPane" class="tab-pane" expression-Panel></div>
		</div>
			
	</div>
		
</div>
