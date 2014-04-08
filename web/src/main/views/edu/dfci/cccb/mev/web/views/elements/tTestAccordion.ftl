	    <accordion-group heading="{{tTest.type}} : {{tTest.name}}" is-open="isTTestOpen" ng-init="isTTestOpen=false">	   
	    	
		            <div  class="results-wrapper" id="tTestResultsTable" ng-hide="!tTest.results">	
			        	<div class="results-header clearfix">
			        	                    				
		                    				<h3 class="pull-left analysis-results-header">Results</h3>
		                    				<div class="btn-toolbar pull-right" role="toolbar">
    
	                            				<button class="btn btn-success " >
									                <a href="/dataset/{{datasetName}}/analysis/{{tTest.name}}?format=tsv">
									                  <i class="icon-white icon-download"></i> Download
									                </a> 
									            </button>
									            <button class="btn btn-info " >
									                <a data-target="#selectionAdd{{tTest.name}}" data-toggle="modal">
									                  </i> Create Selections From Results
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
															    	 ID <span ng-class="getCaretCss(headers['ID'])"  />
															    </div>
															    <div class="input-group">
															   		<input type="text" class="form-control input-small" ng-model="filterParams.id" placeholder="Filter">
															   	</div>																
					                                      	</div>
					                                      </th>
					                                      
					                                      <th>					                                      	
					                                      	<div>
					                                      		<div ng-click="reorderTTestTable('P-Value')">
															    	 <span ng-class="getCaretCss(headers['P-Value'])" ></span>P-Value
															    </div>
															    <div class="input-group">
																    <span class="input-group-addon">&lt;=</span>
																	<input type="text" class="form-control input-small" placeholder="(ex: 0.05)" ng-model="filterParams.pValueThreshold">
																</div>																			                            			
					                                      	</div>					                                      	
					                                      </th>
					                                      
					                                      <th>								                                      
					                                      	<div>
					                                      		<div ng-click="reorderTTestTable('Log Fold Change')">
															    	 <span ng-class="getCaretCss(headers['Log Fold Change'])" ></span>Log Fold Change
															    </div>
															    <div class="input-group">
																    <span class="input-group-addon">&gt;=</span>
																	<input type="text" class="form-control input-small" placeholder="(ex: 2.0)" ng-model="filterParams.logFoldChange">
																</div>																			                            			
					                                      	</div>					                                      	
					                                      </th>
					                                      
					                                      
					                                    </tr>
					                                    </thead>
					                                    <tbody>
							                                    <tr ng-repeat="row in tTest.results | filter: filterParams.id | filterThreshold: filterParams.pValueThreshold : 'pValue' | 
							                                    filterThreshold: filterParams.logFoldChange : 'logFoldChange' : '>=' | orderBy: tTestTableOrdering ">
							                                            <td>
							                                                    {{row["id"]}}
							                                            </td>

							                                            <td>
							                                                    {{row["pValue"] | number:4}} 
							                                            </td>
							                                            
							                                            <td>
							                                                    {{row["logFoldChange"] | number:4}} 
							                                            </td>

							                                    </tr>
							                            </tbody>
						                            </table>
                    			</form>
								</form-group>
						</div>
					</div>	
				
		</accordion-group>
		
		<bsmodal bindid="selectionAdd{{tTest.name}}" func="" header="Add New Selection for {{tTest.name}}">

			<div class="row">
			
				<form-group>
					<form>
						Name: <input type="text" class="input-small" ng-model="selectionParams.name">
					<form>
				</form-group>
			
			</div>
			
			<div class="row">
			
	            <button class="btn btn-success pull-right" >
	                <a ng-click="addSelections()" data-dismiss="modal" aria-hidden="true">
	                  Create Selections From Results
	                </a> 
	            </button>
            </div>
		</bsmodal> 