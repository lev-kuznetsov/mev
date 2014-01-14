<style>

.fixed-height {
  overflow:auto;
}

</style>

	

	<div class="tabbable">
	
		<ul class="nav nav-tabs">
		  <li class="active"><a id="heatmapTabLink" href="#heatmap-tab" data-toggle="tab">Visualize</a></li>
		  <li id="annotationsTab">
			 <a id="annotationsTabLink" href="#annotationsTabPane" data-toggle="tab">Annotations</a>
		  </li>
		  <li><a href="#selections-tab" data-toggle="tab">Selections</a></li>
		  <li><a href="#clusters-tab" data-toggle="tab">Clusters</a></li>
		  <li><a href="#limma-tab" data-toggle="tab">LIMMA</a></li>
		</ul>
	
		<div class="tab-content">
			    
			    
			 <div id="annotationsTabPane" class="tab-pane">			    
			    	<!-- my-iframe id="annotationsIframe" height="70%" width="99%"></my-iframe -->
			    	<iframe scrolling="no" frameborder="0" width="99%" height="80%" ng-src="{{annotationsUrl}}" />
			 </div>
		
			<div class="tab-pane active" id="heatmap-tab">
			  <div class="fixed-height">
		        <vis-Heatmap></vis-Heatmap>
		      </div>
			</div>
			<div class="tab-pane" id="selections-tab">
			
				<selection-set-manager id="selectionSetMgr" ng-controller="SelectionSetManagerCtl" heatmap-data="heatmapData" heatmap-id="{{heatmapId}}"></selection-set-manager>
			
			</div> <!-- End Selections Tab Content -->
			
			<div class="tab-pane" id="clusters-tab">
			
				<cluster-Accordion-List></cluster-Accordion-List>
			
			</div> <!-- End Cluster Tab -->
			
			<div class="tab-pane" id="limma-tab">
			
				<limma-Accordion-List></limma-Accordion-List>
			
			</div> <!--End Limma Tab -->
		
		</div> <!-- End Tab Content -->
		
	</div> <!-- End Tabbable -->
