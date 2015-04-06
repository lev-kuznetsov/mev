(function () {

    define([], function () {


        return function (module) {

            module.directive('integerRange', function() {
            	  return {
            		  	element: "CA",
            		    require: 'ngModel',
            		    link: function(scope, elm, attrs, ctrl) {
            		      ctrl.$parsers.unshift(function(viewValue) {
            		        if (parseFloat(viewValue) <= parseFloat(attrs.max) && 
            		        		viewValue >= parseFloat(attrs.min)) {
            		          // it is valid
            		          ctrl.$setValidity('integer', true);
            		          return viewValue;
            		        } else {
            		          // it is invalid, return undefined (no model update)
            		          ctrl.$setValidity('integer', false);
            		          return undefined;
            		        }
            		      });
            		    }
            		  };
            		})
            .directive('modalNmf', ['alertService', 'pathModalService', function(alertService, paths) { 
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
		                        rank: 3,
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
	                    	
	                    	if (typeof scope.params.rank == 'undefined' ){
	                    		alertService.error("Cannot create NMF with rank greater than 20",
	                    				"Non-Negative Matrix Factorization")
	                    		return
	                    	}
	                        
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
	                        });
	                    };
	                }
	            }
            }])


            return module

        }

    })

})()