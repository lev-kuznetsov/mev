<accordion-group heading="{{analysis.type}} : {{analysis.name}}" is-open="isAccordionOpen" ng-init="isAccordionOpen=false">

	<div  class="results-wrapper">	
    	<div class="results-header clearfix">
    	                    				
			<h3 class="pull-left analysis-results-header">Results</h3>
			<div class="btn-toolbar pull-right" role="toolbar">

				<button class="btn btn-success " >
	                <a href="/dataset/{{datasetName}}/analysis/{{analysis.name}}?format=tsv">
	                  <i class="icon-white icon-download"></i> Download
	                </a> 
	            </button>
	            
	            <button class="btn btn-info" >
	                <a data-target="#selectionAdd{{analysis.name}}" data-toggle="modal">
	                  </i> Create Selections From Results
	                </a> 
	            </button>
	            
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
	                      <th ng-repeat="header in headers">
	                      	
							    <p ng-click="reorderTable(header)">
							    	<span class="caret" ></span>{{header.name}}
							    	 
							    	 <div class="input-group">
									   		<span ng-show="header.value != 'id' && header.value != 'pairwise_log_fold_change'" class="input-group-addon">&lt;=</span>
									   		<span ng-hide="header.value != 'id'" class="input-group-addon"><span class="glyphicon glyphicon-search"></span></span>
								    	 	<input ng-hide="header.value === 'pairwise_log_fold_change'" type="text" class="form-control placeholder="(ex: 0.05)" input-small" ng-model="filterParams[header.value]">
							    	 	
							    	 </div>
								</p>
								
	                      	
	                      </th>
	                    </tr>
					</thead>
                    <tbody>
                            <tr ng-repeat="row in analysis.results | filter:filterParams.id |  filterThreshold: filterParams.pValue : 'pValue' : '<=' | orderBy: tableOrdering ">
                                    <td>
                                            {{row.id}}
                                    </td>
                                    
                                    <td>
                                            <p title="{{row['pValue']}}">{{row["pValue"] | number:4}}</p> 
                                    </td>
                                    
                                    <td>
                                    	<p ng-repeat="pair in row.pairwise_log_fold_change">{{pair.partnerA}}/{{pair.partnerB}} : {{pair.ratio}}</p>
                                    </td>
                                    
                            </tr>
                    </tbody>
                </table>
			</form>
			</form-group>
		</div>
	</div>	          

</accordion-group>



<bsmodal bindid="{{'selectionAdd' + analysis.name}}" func="" header="Add New Selection for {{analysis.name}}">

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