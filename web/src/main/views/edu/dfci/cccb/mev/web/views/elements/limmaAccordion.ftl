<accordion-group heading="{{limma.datar.type}} : {{limma.name}}" is-open="isLimmaOpen" ng-init="isLimmaOpen=false">

	<div  class="results-wrapper">	
		<div class="results-header clearfix">
		                    				
			<h3 class="pull-left analysis-results-header">Results</h3>
			<div class="btn-toolbar pull-right" role="toolbar">
				<button class="btn btn-success" >
	                <a href="/dataset/{{datasetName}}/analysis/{{limma.name}}?format=tsv">
	                  <i class="icon-white icon-download"></i> Download
	                </a> 
	            </button>
	            
	            <button class="btn btn-info" >
	                <a data-target="#selectionAdd{{limma.name}}" data-toggle="modal">
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
	                          <th ng-repeat="header in headers">
	                          	
	                          		<p ng-click="reorderLimmaTable(header.value)">
								    	 <span class="caret" ></span>{{header.name}}
								    	 <div class="input-group" ng-hide="header.icon == 'undefined'">
								    		<span class="input-group-addon" ng-hide="header.icon != 'search'"><span class="glyphicon glyphicon-search"></span></span>
								   			<span class="input-group-addon" ng-hide="header.icon == 'search'">{{header.icon}}</span>
								   			
								   			<input type="text" class="form-control input-small" ng-model="filterParams[header.value]">
								   		</div>	
									</p>
									
	                          	
	                          </th>
	                        </tr>
						</thead>
                        <tbody>
                                <tr ng-repeat="row in limma.datar.results |filter:filterParams.id| filterThreshold: filterParams.pValue : 'pValue' | filterThreshold: filterParams.logFoldChange : 'logFoldChange' : '>=' | orderBy: tableOrdering ">
                                        <td>
                                                {{row["id"]}}
                                        </td>
                                        <td>
                                                <p title="{{row['logFoldChange']}}">{{row["logFoldChange"] | number:4}}</p>
                                        </td>
                                        <td>
                                                <p title="{{row['averageExpression']}}">{{row["averageExpression"] | number:4}}<p>
                                        </td>
                                        <td>
                                                <p title="{{row['pValue']}}">{{row["pValue"] | number:4}}</p>
                                        </td>
                                        <td>
                                                <p title="{{row['qValue']}}">{{row["qValue"] | number:4}}</p>
                                        </td>
                                </tr>
                        </tbody>
                    </table>
				</form>
				</form-group>
		</div>
	</div>
</accordion-group>
		
<bsmodal bindid="selectionAdd{{limma.name}}" func="" header="Add New Selection for {{limma.name}}">

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