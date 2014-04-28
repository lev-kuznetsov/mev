               <ul class="nav nav-tabs">
                       <li class="active"><a id="heatmapTabLink" href="#heatmaptabpane" data-toggle="tab" target="_self">Visualize</a></li>
                       <li><a id="annotationsTabLink" href="#annotationsTabPane" data-toggle="tab" target="_self">Annotations</a></li>
               </ul>
       
               <div class="tab-content">
               
                       <div class="tab-pane active" id="heatmaptabpane">
                               <vis-Heatmap
                               		heatmap-dataset="dataset.views[0]">
                               	</vis-Heatmap>
                       </div>
                           
                       <div class="tab-pane" id="annotationsTabPane">                      
                               <!-- my-iframe id="annotationsIframe" height="100%" width="99%"></my-iframe -->
                               <iframe scrolling="no" frameborder="0" width="99%" height="100%" ng-src="{{annotationsUrl}}" />
                       </div>
               
               </div> <!-- End Tab Content -->
