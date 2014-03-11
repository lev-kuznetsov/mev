

<div class="panel-group" id="{{limma.parentId}}" ng-repeat="limma in previousLimmaAnalysis">
  <div class="panel panel-default">
    <div class="panel-heading">
      <h4 class="panel-title">
        <a data-toggle="collapse" data-parent="{{limma.dataParent}}" href="{{limma.href}}">
          {{limma.datar.type}} : {{limma.name}} 
        </a>
      </h4>
    </div>
    <div id="{{limma.divId}}" class="panel-collapse">
      <div class="panel-body">
      
        	<div class="container">
	            <div class="col-md-12>
	            
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
		                                      	<p ng-click="reorderLimmaTable(tableHeader)"> {{tableHeader}}</p>
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
                
                </div> <!-- End Span -->
                </div> <!-- End Row -->
	        
      </div>
    </div>
  </div>
</div>


<div class="accordion" id="{{limma.parentId}}" ng-repeat="limma in previousLimmaAnalysis">
    <div class="accordion-group">
    
	    <div class="accordion-heading">
	      <a class="accordion-toggle" data-toggle="collapse" data-parent="{{limma.dataParent}}" href="{{limma.href}}">
	        {{limma.datar.type}} : {{limma.name}}
	      </a>
	    </div> <!-- End Heading Div -->
	    
	    <div id="{{limma.divId}}" class="accordion-body collapse">
	      <div class="accordion-inner">
	            
	            
	        
	      </div>
	    </div> <!-- End Body Div -->
    
    </div> <!-- End Accordion Grouping -->
</div> <!-- End Accordion Definition -->
