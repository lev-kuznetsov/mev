               <ul class="nav nav-tabs">
                       
                       <li class="active"><a id="annotationsTabLink" href="#annotationsTabPane" data-toggle="tab" target="_self">Annotations</a></li>

                       <li ng-repeat="view in project.views">
                           <a id="heatmapTabLink" href="{{'#' + view.id}}" 
                           data-toggle="tab" 
                           target="_self">Visualize</a>
                       </li>
               </ul>
       
               <div class="tab-content" id="heatmappanecontainer">
               
               		<div class="tab-pane" ng-repeat='view in project.views' id="{{view.id}}">
	                       <view-Content-Item project='project' view='view'>
								</view-Content-Item>
                    </div>
                           
                    <div class="tab-pane active" id="annotationsTabPane">                      
                               <!-- my-iframe id="annotationsIframe" height="100%" width="99%"></my-iframe -->
                               <iframe scrolling="no" frameborder="0" width="99%" height="100%" ng-src="{{annotationsUrl}}" />
                    </div>
               
               </div> <!-- End Tab Content -->
