<accordion close-others="false">
	    <accordion-group ng-repeat="limma in previousLimmaAnalysis" heading="{{limma.datar.type}} : {{limma.name}}" is-open="false">
	    	<div class="col-md-12">
	    	
	    			<div class="row">
		              <div class="pull-right">
		              
		                <button class="btn btn-success" >
			                <a href="/dataset/{{datasetName}}/analysis/{{limma.name}}?format=tsv">
			                  <i class="icon-white icon-download"></i> Download
			                </a> 
			            </button>
		              
		              </div>	    			
	    			</div>
	    			<br>
		            
		            <div class="row">
			        	<div class="limma-table" id="limmaResultsTable" ng-hide="!limma.datar.results || !showLimmaTables">
			        	
		                    <table class="table table-bordered">
		                            		<tr>
		                            			<td>
		                            				<h3>Filtering</h3>
		                            				<hr>
		                            				<form-group>
		                            					<form>
		                            						Genes: <input type="text" class="input-small" ng-model="search.id">
		                            					<form>
													</form-group>
													<br>
													<br>
													<form-group>
														<form class="form-inline">
															Threshold: <input type="text" class="input-small" placeholder="P-Value" ng-model="pvalueThreshold">
															<input type="text"  class="input-small" placeholder="Log Fold" ng-model="logFoldThreshold">
		                            					</form>
		                            				</form-group>
		                            				
		                            				<h3>Results</h3>
		                            				<hr>
		                            				
		                            				<table class="table table-striped table-bordered">
					                                    <tr>
					                                      <th ng-repeat="tableHeader in ['ID', 'Log-Fold-Change', 'Average Expression', 'P-Value', 'Q-Value']">
					                                      	
					                                      	<div class="dropdown">
					                                      		<p class="dropdown-toggle" data-toggle="dropdown" id="dropdownMenu">
															    	 {{tableHeader}}<span class="caret" ng-click="reorderLimmaTable(tableHeader)"></span>
																</p>
																
					                                      	</div>
					                                      	
					                                      </th>
					                                    </tr>
	
							                            <tbody>
							                                    <tr ng-repeat="row in limma.datar.results |filter:search| filterThreshold: pvalueThreshold : 'pValue' | filterThreshold: logFoldThreshold : 'logFoldChange' | orderBy: limmaTableOrdering ">
							                                            <td>
							                                                    {{row["id"]}}
							                                            </td>
							                                            <td>
							                                                    {{row["logFoldChange"] | number:4}}
							                                            </td>
							                                            <td>
							                                                    {{row["averageExpression"] | number:4}}
							                                            </td>
							                                            <td>
							                                                    {{row["pValue"] | number:4}} 
							                                            </td>
							                                            <td>
							                                                    {{row["qValue"] | number:4}}
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
		
</accordion>

