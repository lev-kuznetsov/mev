<accordion close-others="false">
	<div  ng-repeat="limma in previousLimmaAnalysis">
	    <accordion-group heading="{{limma.datar.type}} : {{limma.name}}" is-open="false">

	            
		            <div class="row">
		            
		              <div class="pull-left">
		              
		                <button class="btn btn-success" >
			                <a href="/dataset/{{datasetName}}/analysis/{{limma.name}}?format=tsv">
			                  <i class="icon-white icon-download"></i> Download
			                </a> 
			            </button>
		              
		              </div>
			            
			          <div class="pull-right">
			            <form class="form-inline">
						  Thresholds: <input type="text" class="input-small" placeholder="P-Value" ng-model="pvalueThreshold">
						  <input type="text"  class="input-small" placeholder="Log Fold" ng-model="logFoldThreshold">
						 
						</form>
					  </div>
			            
			            <div id="limmaResultsNotSignificant" ng-hide="limma.datar.results">
			              <hr>
			              <p>No Results!</p>
			              
			            </div>
			            
			        </div>
		            
		            <div class="row">
			        	<div class="limma-table" id="limmaResultsTable" ng-hide="!limma.datar.results || !showLimmaTables">
			        	
		                    <table class="table table-striped table-bordered">
		                            <thead>
		                                    <tr>
		                                      <th ng-repeat="tableHeader in ['ID', 'Log-Fold-Change', 'Average Expression', 'P-Value', 'Q-Value']">
		                                      	
		                                      	<div class="dropdown">
		                                      		<p class="dropdown-toggle" data-toggle="dropdown" id="dropdownMenu">
												    	 {{tableHeader}}<span class="caret"></span>
													</p>
													<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu">
													
														<li>
													      <p ng-click="reorderLimmaTable(tableHeader)">Order By</p>
													    </li>
													    
													</ul>
		                                      	</div>
		                                      	
		                                      </th>
		                                    </tr>
		                            </thead>
		                            <tbody>
		                                    <tr ng-repeat="row in limma.datar.results | filterThreshold: pvalueThreshold : 'pValue' | filterThreshold: logFoldThreshold : 'logFoldChange' | orderBy: limmaTableOrdering ">
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
		                </div>
		            </div>
                

		</accordion-group>
	</div> <!-- repeat -->
</accordion>
