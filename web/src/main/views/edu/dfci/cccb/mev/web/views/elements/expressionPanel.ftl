<style>

.fixed-height {
  overflow:auto;
}

</style>


	<div class="tabbable">
	
		<ul class="nav nav-tabs">
		  <li class="active"><a href="#heatmap-tab" data-toggle="tab">Visualize</a></li>
		  <li><a href="#selections-tab" data-toggle="tab">Selections</a></li>
		  <li><a href="#clusters-tab" data-toggle="tab">Clusters</a></li>
		  <li><a href="#limma-tab" data-toggle="tab">LIMMA</a></li>
		</ul>
	
		<div class="tab-content">
		
			<div class="tab-pane active" id="heatmap-tab">
			  <div class="fixed-height">
		        <vis-Heatmap></vis-Heatmap>
		      </div>
			</div>
			<div class="tab-pane" id="selections-tab">
			
				<div id="setmanagerAccordion" class="accordion">

					<div class="accordion-group">
				    
					    <div class="accordion-heading">
					      <a class="accordion-toggle" data-toggle="collapse" href="#collapseSetManagerColumns">
					        Column Sets
					      </a>
					      <a class="accordion-action-button" href="/annotations/{{heatmapId}}/annotation/column/new/">create new from annotations</a>
					    </div> <!-- End Heading Div -->
					    
					    <div id="collapseSetManagerColumns" class="accordion-body collapse" >
					      <div class="accordion-inner">
					        <div class="selectionSetList">
					        	<div ng-show="theData.column.selections.length>0" ng-repeat="selection in theData.column.selections" class="selectionSetListItem">
					        		<div class="selectionSetColor" style='background-color: {{selection.properties.selectionColor}}'></div>
					        		<div><a href="/annotations/{{heatmapId}}/annotation/column/{{selection.name}}/{{selection.properties.selectionFacetLink}}">{{selection.name}}</a></div>
					        		<div class="selectionSetDescription">{{selection.properties.selectionDescription}}</div>
					        		<div class="selectionSetKeys">
						        		<span ng-repeat="key in selection.keys">
						        			{{key}},
						        		</span>
					        		</div>				        		
					        	</div>
					        	<div ng-show="theData.column.selections.length<=0" class="selectionSetListItem">No sets defined. You may define sets using <a href="/annotations/{{heatmapId}}/annotation/column/new/">column annotations</a> or by performing a clustering analysis.</div>
					      </div>
					    </div>
				      </div> <!-- End Accordion Body -->
				    </div> <!-- End Accordion Grouping -->
				    
				    <div class="accordion-group">
    
					    <div class="accordion-heading">
					      <a class="accordion-toggle" data-toggle="collapse" href="#collapseSetManagerRows">
					        Row Sets
					      </a>				      
					    </div> <!-- End Heading Div -->
					    
					    <div id="collapseSetManagerRows" class="accordion-body collapse" >
					      <div class="accordion-inner">
					        <div class="selectionSetList">
					        	<div ng-show="theData.row.selections.length>0" ng-repeat="selection in theData.row.selections" class="selectionSetListItem">
					        		<div class="selectionSetColor" style='background-color: {{selection.properties.selectionColor}}'></div>
					        		<div><a href="/annotations/{{heatmapId}}/annotation/row/{{selection.properties.selectionFacetLink}}&{{selection.name}}&{{selection.properties.selectionColor}}&{{selection.properties.selectionDescription}}">{{selection.name}}</a></div>
					        		<div class="selectionSetDescription">{{selection.properties.selectionDescription}}</div>
					        		<div class="selectionSetKeys">
						        		<span ng-repeat="key in selection.keys">
						        			{{key}},
						        		</span>
					        		</div>				        		
					        	</div>
					        	<div ng-show="theData.row.selections.length<=0" class="selectionSetListItem">No sets defined. You may define sets by performing a clustering analysis.</div>
					        </div>
					      </div>
					    </div> <!-- End Accordion Body -->
				    
				    </div> <!-- End Accordion Grouping -->
				    			    
				</div> <!-- End Accordion Definition -->
			
			</div> <!-- End Accordion Tab Content -->
			
			<div class="tab-pane" id="clusters-tab">
			
				<div class="accordion" id="{{cluster.parentId}}" ng-repeat="cluster in previousHCLClusters">
				    <div class="accordion-group">
				    
					    <div class="accordion-heading">
					      <a class="accordion-toggle" data-toggle="collapse" data-parent="{{cluster.dataParent}}" href="{{cluster.href}}">
					        {{cluster.datar.type}} : {{cluster.name}} 
					      </a>
					    </div> <!-- End Heading Div -->
					    
					    <div id="{{cluster.divId}}" class="accordion-body collapse">
					      <div class="accordion-inner">
					      
					        <div class="row-fluid">
					          <div class="span12">
					          <button class="btn btn-success pull-right" ng-click="updateHeatmapData(cluster.name, cluster.datar)">
					           Apply to heatmap <i class='icon-chevron-right'></i>
					          </button>
					          </div>
					        </div>
					        <br>
					        <div class="row-fluid">
					          <div class="span12">
					            <div d3-Radial-Tree data="cluster.datar" diameter='400'></div> 
					          </div>
					        </div>
					        
					    </div> <!-- End Body Div -->
				    
				    </div> <!-- End Accordion Grouping -->
				</div> <!-- End Accordion Definition -->
			
			</div> <!-- End Cluster Tab -->
			
			<div class="tab-pane" id="limma-tab">
			
				<div class="accordion" id="{{cluster.parentId}}" ng-repeat="limma in previousLimmaAnalysis">
				    <div class="accordion-group">
				    
					    <div class="accordion-heading">
					      <a class="accordion-toggle" data-toggle="collapse" data-parent="{{limma.dataParent}}" href="{{limma.href}}">
					        {{limma.datar.type}} : {{limma.name}}
					      </a>
					    </div> <!-- End Heading Div -->
					    
					    <div id="{{limma.divId}}" class="accordion-body collapse">
					      <div class="accordion-inner">
					            
					            <div class="row-fluid">
					            <div class="span12>
					            
						            <div class="row-fluid">
							            <button class="btn pull-right btn-success" >
							              <a href="/dataset/{{datasetName}}/analysis/{{limma.name}}?format=tsv">
							              <i class="icon-white icon-download"></i> Download
							              </a> 
							            </button>
							            
							            <div id="limmaResultsNotSignificant" ng-hide="limma.datar.significant">
							              <hr>
							              <p>No Significant values!</p>
							              
							            </div>
							            
							            <div id="showTableDialog" ng-hide="showLimmaTables">
							              <a href="" ng-click="expandRight()">Expand to see table...</a>
							            </div>
							            
							        </div>
						            
						            <div class="row-fluid">
							        	<div class="limma-table" id="limmaResultsTable" ng-hide="!limma.datar.significant || !showLimmaTables">
							        	
						                    <table class="table table-striped table-bordered">
						                            <thead>
						                                    <tr>
						                                      <th ng-repeat="header in ['ID', 'Log-Fold-Change', 'Average Expression', 'P-Value', 'Q-Value']">{{header}}</th>
						                                    </tr>
						                            </thead>
						                            <tbody>
						                                    <tr ng-repeat="row in limma.datar.significant">
						                                            <td>
						                                                    {{row["id"]}}
						                                            </td>
						                                            <td>
						                                                    {{row["logFoldChange"]}}
						                                            </td>
						                                            <td>
						                                                    {{row["averageExpression"]}}
						                                            </td>
						                                            <td>
						                                                    {{row["pValue"]}}
						                                            </td>
						                                            <td>
						                                                    {{row["qValue"]}}
						                                            </td>
						                                    </tr>
						                            </tbody>
						                    </table>
						                </div>
						            </div>
				                
				                </div> <!-- End Span -->
				                </div> <!-- End Row -->
					        
					      </div>
					    </div> <!-- End Body Div -->
				    
				    </div> <!-- End Accordion Grouping -->
				</div> <!-- End Accordion Definition -->
			
			</div> <!--End Limma Tab -->
		
		</div> <!-- End Tab Content -->
		
	</div> <!-- End Tabbable -->
