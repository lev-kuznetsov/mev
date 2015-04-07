(function () {

    define([], function () {


        return function (module) {

            module.directive('modalPca', ['alertService', 'pathModalService', function(alertService, paths) { 
	            return {
	                restrict : 'C',
	                scope : {
	                	dataset : '=dataset'
	                },
	                templateUrl : paths.module + '/templates/pca.tpl.html',
	                link : function(scope, elems, attrs) {
	                    
	                    scope.params = {
	                        name: undefined,
	                        selections: [],
	                        dimension: {
	                            name : 'Columns',
	                            value : 'column'
	                        },
	                        pvalue: undefined,
	                        mtc: false
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
	                    
	                    scope.$watch('params.dimension', function(newval){
	                        if(newval){
	                            scope.params.selections = []
	                        }
	                    })

	                    var parametersOK = function(){

		    				var failing = (!scope.params.name)
		
		    				return !failing
		    			}
	                    
	                    scope.initialize = function(){
	                        
	                        var message = "Starting PCA for "
	                            + scope.params.name;
	
	                        var header = "Principal Components Analysis";

	                        if (!parametersOK()) {
	                            
	                            message = "Bad analysis parameters selection";
	
	                            header = "PCA Start Error";
	                            
	                            alertService.error(message, header);
	                            return
	                        }
	                        
	                        if (scope.params.selections.length < 1) {
	                            
	                            message = "Can't start PCA for "
	                                + scope.params.name + " with less than one group.";
	
	                            header = "Principal Components Analysis";
	                            
	                            alertService.error(message,header);
	                            return
	                        }
	                        
	                        alertService.info(message,header);
	                        
	                        var analysisData = {
	                        	datasetName : scope.dataset.datasetName,
	                        	analysisType : "pca",
	                        	analysisName : scope.params.name,
	                        	analysisParams : 'dimension='
	                                + scope.params.dimension.value
	                        }
	                        
	                        scope.dataset.analysis
	                        .postf(analysisData, JSON.stringify(scope.params.selections),
		                        function(data, status, headers, config) {
		                            
	                        		scope.dataset.loadAnalyses();
	                        		
		                            var message = "PCA for "
		                            	+ scope.params.name + " complete!";
		
		                            var header = "Principal Components Analysis";
		                                         
		                            alertService.success(message,header);
		                                    
		                         }, function(data, status, headers, config) {
		                            
		                            var message = "Could not perform Principal Components Analysis. If "
		                                + "problem persists, please contact us.";
		                            var header = "Clustering Problem (Error Code: "
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
