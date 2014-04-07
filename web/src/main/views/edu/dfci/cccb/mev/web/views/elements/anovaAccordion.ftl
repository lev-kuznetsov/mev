<accordion-group heading="{{analysis.type}} : {{analysis.name}}" is-open="isAccordionOpen" ng-init="isAccordionOpen=false">
	    	<div class="col-md-12">
	    	
	    			<div class="row">
		              <div class="pull-right">
		              
		                <button class="btn btn-success" >
			                <a href="/dataset/{{datasetName}}/analysis/{{analysis.name}}?format=tsv">
			                  <i class="icon-white icon-download"></i> Download
			                </a> 
			            </button>
		              
			            
		              
		              </div>	    			
	    			</div>
	    			<br>
		            
		            <div class="row">
			        	
		                    <table class="table table-bordered">
		                            		<tr>
		                            			<td>
		                            				
		                            				<h3>Results</h3>
		                            				<hr>
		                            				
		                            				<table class="table table-striped table-bordered">
					                                    <tr>
					                                      <th ng-repeat="header in headers">
					                                      	
					                                      	<div class="dropdown">
					                                      		<p class="dropdown-toggle" data-toggle="dropdown" id="dropdownMenu">
															    	 <p ng-click="reorderTable(header)">{{header.name}}<span class="caret" ></span></p>
															    	 <input type="text" class="input-small" ng-model="filterParams[header.value]">
																</p>
																
					                                      	</div>
					                                      	
					                                      </th>
					                                    </tr>
	
							                            <tbody>
							                                    <tr ng-repeat="row in analysis.results | filter:filterParams.id | filterThreshold: filterParams.pValueThreshold : 'pValue' | orderBy: tableOrdering ">
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
		</accordion-group>