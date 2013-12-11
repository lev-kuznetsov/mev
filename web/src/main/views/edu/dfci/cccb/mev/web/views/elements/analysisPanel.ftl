
<div class="row-fluid">

  <div class="accordion" id="prevAnalysisList" ng-repeat="cluster in previousClusters">
    <div class="accordion-group">
    
	    <div class="accordion-heading">
	      <a class="accordion-toggle" data-toggle="collapse" data-parent="#prevAnalysisList" href="{{cluster.href}}">
	        {{cluster.name}}
	      </a>
	    </div> <!-- End Heading Div -->
	    
	    <div id="{{cluster.divId}}" class="accordion-body collapse">
	      <div class="accordion-inner">
	        <div d3-Radial-Tree data="cluster.datar" diameter='400'></div> 
	        
	      </div>
	    </div> <!-- End Body Div -->
    
    </div> <!-- End Accordion Grouping -->
  </div> <!-- End Accordion Definition -->

</div> <!-- End Row Definition -->

    <!--
	        <div id="limmaResultsTable">
                    <table class="table table-hover table-bordered">
                            <thead>
                                    <tr>
                                      <th ng-repeat="header in ['ID', 'Log-Fold-Change', 'Average Expression', 'P-Value', 'Q-Value']">{{header}}</th>
                                    </tr>
                            </thead>
                            <tbody>
                                    <tr ng-repeat="row in limmaviewtablerows ">
                                            <td>
                                                    {{row["id"]}}
                                            </td>
                                            <td>
                                                    {{row["logFoldChange"]}}
                                            </td>
                                            <td>
                                                    {{row["averageExpression"]}}
                                            </td>
                                            <td>
                                                    {{row["pValue"]}}
                                            </td>
                                            <td>
                                                    {{row["qValue"]}}
                                            </td>
                                    </tr>
                            </tbody>
                    </table>
            </div> 
	-->
	
