
	<div class="tabbable">
	
		<ul class="nav nav-tabs">
		  <li><a id="heatmapTabLink" href="#heatmap-tab" data-toggle="tab">Visualize</a></li>
		  <li><a id="annotationsTabLink" href="#annotationsTabPane" data-toggle="tab">Annotations</a></li>
		  <li><a id="clustersTabLink" href="#clusters-tab" data-toggle="tab">Clusters</a></li>
		  
		  <li><a id="limmaPane" href="#limma-tab" data-toggle="tab">LIMMA</a></li>
		</ul>
	
		<div class="tab-content">
		
			<div class="tab-pane active fixed-height" id="heatmap-tab">
			  
		        <vis-Heatmap></vis-Heatmap>
		      <br>
			</div>
			    
			 <div id="annotationsTabPane" class="tab-pane">			    
			    	<!-- my-iframe id="annotationsIframe" height="70%" width="99%"></my-iframe -->
			    	<iframe scrolling="no" frameborder="0" width="99%" height="80%" ng-src="{{annotationsUrl}}" />
			 </div>
		
			
			
			<div id="clusters-tab" class="tab-pane fixed-height">
			
				<cluster-Accordion-List></cluster-Accordion-List>
			<br>
			</div> <!-- End Cluster Tab -->
			
			<div id="limma-tab" class="tab-pane fixed-height">
				
				<limma-Accordion-List></limma-Accordion-List>
			<br>
			</div> <!--End Limma Tab -->
		
		</div> <!-- End Tab Content -->
		
	</div> <!-- End Tabbable -->
