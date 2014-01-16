
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
		
			<div class="tab-pane active fixed-height" id="heatmap-tab">
			  
		        <vis-Heatmap></vis-Heatmap>
		      
			</div>
			    
			 <div id="annotationsTabPane" class="tab-pane">			    
			    	<!-- my-iframe id="annotationsIframe" height="70%" width="99%"></my-iframe -->
			    	<iframe scrolling="no" frameborder="0" width="99%" height="80%" ng-src="{{annotationsUrl}}" />
			 </div>
		
			
			
			<div class="tab-pane fixed-height" id="clusters-tab">
			
				<cluster-Accordion-List></cluster-Accordion-List>
			
			</div> <!-- End Cluster Tab -->
			
			<div class="tab-pane fixed-height" id="limma-tab">
				
				<limma-Accordion-List></limma-Accordion-List>
			
			</div> <!--End Limma Tab -->
		
		</div> <!-- End Tab Content -->
		
	</div> <!-- End Tabbable -->
