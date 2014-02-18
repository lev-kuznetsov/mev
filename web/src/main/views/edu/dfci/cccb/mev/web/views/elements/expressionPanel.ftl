
<div class="tabbable">
	
		<ul class="nav nav-tabs">
			<li class="active"><a id="heatmapTabLink" href="#heatmaptabpane" data-toggle="tab">Visualize</a></li>
			<li><a id="annotationsTabLink" href="#annotationsTabPane" data-toggle="tab">Annotations</a></li>
			<li><a id="clustersTabLink" href="#clustersTabPane" data-toggle="tab">Clusters</a></li>
			<li><a id="limmaTabLink" href="#limmaTabPane" data-toggle="tab">LIMMA</a></li>
		</ul>
	
		<div class="tab-content">
		
			<div class="tab-pane active fixed-height" id="heatmaptabpane">
				<vis-Heatmap></vis-Heatmap>
			</div>
			    
			<div class="tab-pane fixed-height" id="annotationsTabPane">			    
				<!-- my-iframe id="annotationsIframe" height="70%" width="99%"></my-iframe -->
				<iframe scrolling="no" frameborder="0" width="99%" height="80%" ng-src="{{annotationsUrl}}" />
			</div>

			<div class="tab-pane fixed-height" id="clustersTabPane">
				<cluster-Accordion-List></cluster-Accordion-List>
			</div> <!-- End Cluster Tab -->
			
			<div class="tab-pane fixed-height" id="limmaTabPane">
				<limma-Accordion-List></limma-Accordion-List>
			</div> <!--End Limma Tab -->
		
		</div> <!-- End Tab Content -->
		
</div> <!-- End Tabbable -->
