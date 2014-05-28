<accordion-group heading="{{analysis.type}} : {{analysis.name}}" is-open="isLimmaOpen" ng-init="isLimmaOpen=false">

	<ul class="nav nav-tabs">
		<li class="active">
			<a id="annotationsTabLink" href="{{'#limma-' + analysis.randomId + '-limmaTable'}}" 
			data-toggle="tab" 
			target="_self">Table</a>
		</li>
		<li>
			<a id="heatmapTabLink" href="{{'#limma-' + analysis.randomId + '-boxPlot'}}" 
				data-toggle="tab" 
				target="_self">Quartiles</a>
		</li>
	</ul>
	
	<div class="tab-content" id="">
		<div class="tab-pane active" id="{{'limma-' + analysis.randomId + '-limmaTable'}}">                      
			<div  class="results-wrapper">	
					<div class="results-header clearfix">
					                    				
						<h3 class="pull-left analysis-results-header">Results</h3>
						<div class="btn-toolbar pull-right" role="toolbar">
			                
			                <a class="btn btn-info" data-target="#selectionAdd{{analysis.name}}" data-toggle="modal">
			                  Create Selections
			                </a>
			                
			                <a class="btn btn-info" ng-click="">
			                  View Quartiles
			                </a>
			                
			                <a class="btn btn-success" href="/dataset/{{project.dataset.datasetName}}/analysis/{{analysis.name}}?format=tsv">
			                  <i class="icon-white icon-download"></i> Download
			                </a> 
			            
				            <!-- disable for beta1 -->
				            <button ng-hide="true" class="btn btn-success" ng-click="applyToHeatmap()" >
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
				                          	
				                          		<p ng-click="reorderLimmaTable(header)">
											    	 <span class="caret" ></span>{{header.name}}
											    	 <div class="input-group" ng-hide="header.icon == 'none'">
											    		<span class="input-group-addon" ng-show="header.icon == 'search'"><span class="glyphicon glyphicon-search"></span></span>
											   			<span class="input-group-addon" ng-hide="header.icon | isArray">{{header.icon}}</span>
											   			<span class="input-group-addon" ng-show="header.icon | isArray">
												   			<select ng-model="filterParams[header.field].op">
												   				<option ng-repeat="icon in header.icon track by $index">{{icon}}</option>
												   			</select>
											   			</span>
											   			<input type="text" class="form-control input-small" ng-model="filterParams[header.field].value">
											   		</div>	
												</p>
												
				                          	
				                          </th>
				                        </tr>
									</thead>
			                        <tbody>
			                                <tr ng-repeat="row in applyFilter(analysis.results)">
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
		</div>
		<div class="tab-pane" id="{{'limma-' + analysis.randomId + '-boxPlot'}}">
			<div id="{{'limma-' + analysis.randomId + '-svg-holder'}}"></div>
		</div>
	</div>

	
</accordion-group>


		
<bsmodal bindid="{{'selectionAdd' + analysis.name}}" func="" header="{{'Add New Selection for' + analysis.name}}">

	<div class="row">
	
		<form-group>
			<form>
				Name: <input type="text" class="input-small" ng-model="selectionParams.name">
			<form>
		</form-group>
	
	</div>
	
	<div class="row">
            <a ng-click="addSelections()" data-dismiss="modal" aria-hidden="true" class="btn btn-success pull-right">
              Create Selections
            </a> 
    </div>
</bsmodal> 