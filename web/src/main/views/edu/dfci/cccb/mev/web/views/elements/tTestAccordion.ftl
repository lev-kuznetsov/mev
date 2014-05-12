	    <accordion-group heading="{{tTest.type}} : {{tTest.name}}" is-open="isTTestOpen" ng-init="isTTestOpen=false">	   
	    	
		            <div  class="results-wrapper" id="tTestResultsTable" ng-hide="!tTest.results">		            	
			        	<div class="results-header clearfix">
            				<div class="btn-toolbar pull-right" role="toolbar">
				                <a class="btn btn-success" href="/dataset/{{project.dataset.datasetName}}/analysis/{{tTest.name}}?format=tsv">
				                  <i class="icon-white icon-download"></i> Download
				                </a> 
				                <a class="btn btn-info" data-target="#selectionAdd{{tTest.name}}" data-toggle="modal">
				                  </i> Create Selections
				                </a> 
					            
					            <button class="btn btn-success" ng-click="applyToHeatmap()" >
					                <a>
					                  </i> View Genes on Heatmap
					                </a> 
					            </button>
					            
					      	</div>
									      
						</div>
						<div class="results-body">							
								<form-group>
		                        <form class="form-inline">
		                            				<table class="table table-striped table-bordered table-condensed">
		                            					<thead>
					                                    <tr>
					                                      <th width="33%">
					                                      	
					                                      	<div>
					                                      		<div class="dropdown-toggle" data-toggle="dropdown" id="dropdownMenu" ng-click="reorderTTestTable('ID', $event)"> 
															    	 <span ng-class="getCaretCss(headers['ID'])" ></span>ID
															    </div>
															    <div class="input-group">
															    	<span class="input-group-addon"><span class="glyphicon glyphicon-search"></span></span>
															   		<input type="text" class="form-control input-small" ng-model="filterParams.id.value" placeholder="Filter">
															   	</div>																
					                                      	</div>
					                                      </th>
					                                      
					                                      <th>					                                      	
					                                      	<div>
					                                      		<div ng-click="reorderTTestTable('P-Value')">
															    	 <span ng-class="getCaretCss(headers['P-Value'])" ></span>P-Value
															    </div>
															    <div class="input-group">
																    <span class="input-group-addon">{{filterParams.pValueThreshold.op}}</span>
																	<input type="text" class="form-control input-small" placeholder="(ex: 0.05)" ng-model="filterParams.pValueThreshold.value">
																</div>																			                            			
					                                      	</div>					                                      	
					                                      </th>
					                                      
					                                      <th>								                                      
					                                      	<div>
					                                      		<div ng-click="reorderTTestTable('Log Fold Change')">
															    	 <span ng-class="getCaretCss(headers['Log Fold Change'])" ></span>Log Fold Change
															    </div>
															    <div class="input-group">
																    <span class="input-group-addon"><select ng-model="filterParams.logFoldChange.op"><option>&gt;=</option><option>&lt;=</option></select></span>
																	<input type="text" class="form-control input-small" placeholder="(ex: 2.0)" ng-model="filterParams.logFoldChange.value">
																</div>																			                            			
					                                      	</div>					                                      	
					                                      </th>
					                                      
					                                      
					                                    </tr>
					                                    </thead>
					                                    <tbody>
<!-- 							                                    <tr ng-repeat="row in tTest.results | filter: filterParams.id | filterThreshold: filterParams.pValueThreshold : 'pValue' |  -->
<!-- 							                                    filterThreshold: filterParams.logFoldChange : 'logFoldChange' : '>=' | orderBy: tTestTableOrdering "> -->
							                                    <tr ng-repeat="row in applyFilter(tTest.results)">
							                                            <td>
							                                                    {{row["id"]}}
							                                            </td>

							                                            <td>
							                                                    <p title="{{row['pValue']}}">{{row["pValue"] | number:4}} </p>
							                                            </td>
							                                            
							                                            <td>
							                                                    <p title="{{row['logFoldChange']}}">{{row["logFoldChange"] | number:4}} </p>
							                                            </td>

							                                    </tr>
							                            </tbody>
						                            </table>
                    			</form>
								</form-group>
						</div>
					</div>	
				
		</accordion-group>
		
		<bsmodal bindid="{{'selectionAdd' + tTest.name}}" func="" header="Add New Selection for {{tTest.name}}">

			<div class="row">
			
				<form-group>
					<form>
						Name: <input type="text" class="input-small" ng-model="selectionParams.name">
					<form>
				</form-group>
			
			</div>
			
			<div class="row">			
                <a class="btn btn-success pull-right" ng-click="addSelections()" data-dismiss="modal" aria-hidden="true">
                  Create Selections
                </a> 
            </div>
		</bsmodal> 