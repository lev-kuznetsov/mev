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
	                    
	                    
	                    
	                    scope.options = {
	                        method: [{value: "brunet", name: "Brunet"},
	                                 {value: "lee", name: "Lee"},
	                                 {value: "offset", name: "Offset"},
	                                 {value: "nsNMF", name: "nsNMF"}],
	                        seed: [
	                            {value: "nndsvd", name: "nndSVD"},
								{value: "ica", name: "ICA"},
								{value: "none", name: "None"},
								{value: "random", name: "Random"}
	                        ]
	                    };
	                    
	                    scope.params = {
		                        name: undefined,
		                        rank: {name:3, value: 3},
		                        method: scope.options.method[0],
		                        nrun: {name:10, value:10},
		                        seed: scope.options.seed[0]
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
	                        }
	                        
	                        scope.dataset.analysis
	                        .post3(analysisData, {
	                        	rank: scope.params.rank.value,
	                        	method: scope.params.method.value,
	                        	nruns: scope.params.nrun.value
	                        },
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