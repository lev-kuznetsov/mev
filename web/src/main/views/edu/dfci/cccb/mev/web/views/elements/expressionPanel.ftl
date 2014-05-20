               <ul class="nav nav-tabs">
                       <li class="active"><a id="heatmapTabLink" href="#heatmaptabpane" data-toggle="tab" target="_self">Visualize</a></li>
                       <li><a id="annotationsTabLink" href="#annotationsTabPane" data-toggle="tab" target="_self">Annotations</a></li>
               </ul>
       
               <div class="tab-content" id="heatmappanecontainer">
               
                       <div class="tab-pane active" id="heatmaptabpane">
                       
                       <view-Content-Item project='project'">
							</view-Content-Item>
                       
                               
                       </div>
                           
                       <div class="tab-pane" id="annotationsTabPane">                      
                               <!-- my-iframe id="annotationsIframe" height="100%" width="99%"></my-iframe -->
                               <iframe scrolling="no" frameborder="0" width="99%" height="100%" ng-src="{{annotationsUrl}}" />
                       </div>
               
               </div> <!-- End Tab Content -->
