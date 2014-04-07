	    <accordion-group heading="{{tTest.type}} : {{tTest.name}}" is-open="isTTestOpen" ng-init="isTTestOpen=false">
	    	<div class="col-md-12">	    	
		            <div class="row">
			        	<div class="tTest-table" id="tTestResultsTable" ng-hide="!tTest.results">
			        	
		                    <table class="table table-bordered">
		                    				<tr><td class="results-header">
		                    					<h3 class="pull-left analysis-results-header">Results</h3>
	                            				<button class="btn btn-success pull-right" >
									                <a href="/dataset/{{datasetName}}/analysis/{{tTest.name}}?format=tsv">
									                  <i class="icon-white icon-download"></i> Download
									                </a> 
									            </button>
									            <button class="btn btn-info pull-right" >
									                <a data-target="#selectionAdd{{tTest.name}}" data-toggle="modal">
									                  </i> Create Selections From Results
									                </a> 
									            </button>
										    </td></tr>
		                            		<tr>
		                            			<td>
		                            				<table class="table table-striped table-bordered">
		                            				<form-group>
		                            				<form class="form-inline">
													
					                                    <tr>
					                                      <th>
					                                      	
					                                      	<div class="dropdown">
					                                      		<p class="dropdown-toggle" data-toggle="dropdown" id="dropdownMenu">
															    	 Row ID<span class="caret" ng-click="reorderTTestTable('ID')"></span>
															    	 <input type="text" class="input-small" ng-model="filterParams.id" placeholder="filter row id">
																</p>
					                                      	</div>
					                                      </th>
					                                      <th>
					                                      	
					                                      	<div class="dropdown">
					                                      		<p class="dropdown-toggle" data-toggle="dropdown" id="dropdownMenu">
															    	 P-Value<span class="caret" ng-click="reorderTTestTable('P-Value')"></span>
																			<input type="text" class="input-small" placeholder="filter < Threshold" ng-model="filterParams.pValueThreshold">													
						                            			</p>
																
					                                      	</div>
					                                      	
					                                      </th>
					                                    </tr>
					                                </form>
													</form-group>
							                            <tbody>
							                                    <tr ng-repeat="row in tTest.results |filter:filterParams.id| filterThreshold: filterParams.pValueThreshold : 'pValue' | orderBy: tTestTableOrdering ">
							                                            <td>
							                                                    {{row["id"]}}
							                                            </td>

							                                            <td>
							                                                    {{row["pValue"] | number:4}} 
							                                            </td>

							                                    </tr>
							                            </tbody>
						                            </table>
						                            
		                            			</td>
		                            		</tr>
		                            		

		                    </table>
		                </div>
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