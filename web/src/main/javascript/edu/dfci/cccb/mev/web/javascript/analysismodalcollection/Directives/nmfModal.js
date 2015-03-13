(function () {

    define([], function () {


        return function (module) {

            module.directive('modalNmf', ['alertService', 'pathModalService', function(alertService, paths) { 
	            return {
	                restrict : 'C',
	                scope : {
	                	dataset : '=dataset'
	                },
	                templateUrl : paths.module + '/templates/nmf.tpl.html',
	                link : function(scope, elems, attrs) {
	                    
	                    scope.params = {
	                        name: undefined,
	                        dimension: {
	                            name : 'Columns',
	                            value : 'column'
	                        }
	                    };
	                    
	                    scope.options = {
	                        dimensions : [{
	                            name : 'Rows',
	                            value : 'row'
	                        }, {
	                            name : 'Columns',
	                            value : 'column'
	                        }]
	                    };
	                    
	                    scope.addSelection = function(decked){
	                      if (scope.params.selections.indexOf(decked.name) < 0 && scope.params.selections.length < 3) {
	                          scope.params.selections.push(decked.name)
	                      }
	                    };
	                    
	                    scope.initialize = function(){
	                        
	                        var message = "Starting NMF for "
	                            + scope.params.name;
	
	                        var header = "Non-Negative Matrix Factorization";
	                        
	                        alertService.info(message,header);
	                        
	                        var analysisData = {
	                        	datasetName : scope.dataset.datasetName,
	                        	analysisType : "nmf",
	                        	analysisName : scope.params.name,
	                        	analysisParams : 'dimension='
	                                + scope.params.dimension.value
	                        }
	                        
	                        scope.dataset.analysis
	                        .postf(analysisData, JSON.stringify(scope.params.selections),
		                        function(data, status, headers, config) {
		                            
	                        		scope.dataset.loadAnalyses();
	                        		
		                            var message = "NMF "
		                            	+ scope.params.name + " complete!";
		
		                            var header = "Non-Negative Matrix Factorization";
		                                         
		                            alertService.success(message,header);
		                                    
		                         }, function(data, status, headers, config) {
		                            
		                            var message = "Could not perform Non-Negative Matrix Factorization. If "
		                                + "problem persists, please contact us.";
		                            var header = "Analysis Problem (Error Code: "
		                                + status
		                                + ")";
		                            alertService.error(message,header);
	                            
	                        });
	                    };
	                }
	            }
            }])


            return module

        }

    })

})()