(function(){
	
	var directiveDeps = [
	                     './Directives/pcaModal',
	                     './Directives/nmfModal'
	                     ]
	
	var serviceDeps = [
	                   './Services/pathModalService'
	                   ]
	
	var deps = ['angular', 'alertservice/AlertService']
	
	define(['angular', 'alertservice/AlertService', 
	        './Directives/pcaModal',
	        './Directives/nmfModal',
	        './Services/pathModalService'
	                     ], function(angular){
	
		var module = angular.module('Mev.AnalysisModalCollection', ['Mev.AlertService'])
		
		module.path = "/container/javascript/analysismodalcollection/"
			
		//load each directive deps file onto the module using function arguments only
        for (var index = 0; index < directiveDeps.length; index++){
            arguments[index + deps.length](module)
        }
        
        //load each service deps file onto module
        for(var index = 0; index < serviceDeps.length; index++){
            arguments[index + deps.length + directiveDeps.length](module)
        }
		
		module
		.directive('modalAnova',[ 'alertService',
	        function(alertService) { 
	            return {
	                restrict : 'C',
	                scope : {
	                	dataset : '=heatmapDataset'
	                },
	                templateUrl : "/container/view/elements/anovaModalBody",
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
	                    
	                    scope.$watch('params.pvalue', function(newval){
	                    	if (newval){
	                    		if (parseFloat(newval) > .05) {
	                    			alertService.info("Cannot start ANOVA with P-value greater than .05", "ANOVA Parameter Warning")
	                    		}
	                    	}
	                    })
	                    
	                    var parametersOK = function(){
	                    	
	                    	var notEnoughGroups = scope.params.selections.length < 2
	                    	var sillyPValue = parseFloat(scope.params.pvalue) > .05
		    				
		    				var failing = (!scope.params.name || notEnoughGroups || sillyPValue)
		
		    				return !failing
		    			}
	                    
	                    scope.testInit = function(){
	                        
	                        if (!parametersOK()) {
	                            
	                            message = "Bad analysis parameters selection";
	
	                            header = "ANOVA Start Error";
	                            
	                            alertService.error(message, header);
	                            return
	                        }
	                        var analysisData = {
	                        	datasetName : scope.dataset.datasetName,
	                        	analysisType : "anova",
	                        	analysisName : scope.params.name,
	                        	analysisParams : 'dimension='
	                                + scope.params.dimension.value
	                                + ',pval='
	                                + scope.params.pvalue
	                                + ',mtc='
	                                + scope.params.mtc
	                        }
	                        
	                        
	                        scope.dataset.analysis
	                        .postf(analysisData, JSON.stringify(scope.params.selections));
	                    };
	                }
	            }
	    }])
	    .directive('modalDESeq', ['alertService', function(alertService){
	    	
	    	return {
	    		restrict: 'C',
	    		scope: {
	    			dataset : '=heatmapDataset'
	    		},
	    		templateUrl : module.path + 'templates/deseq.tpl.html',
	    		link : function(scope, elements, attributes){
	    			
	    			scope.params = {
	    				name: undefined,
	    				experiment: undefined,
	    				control: undefined
	    			};
	    			
	    			var selectionWarning = function(){
	    				alertService.error("Cannot start DESeq with intersecting selection parameters", "DESeq Parameters problem")
	    				scope.badSelection = true;
	    			}
	    			
	    			scope.$watch('params.experiment', function(newval){
	    				if (newval && scope.params.control && scope.dataset){
	    					
	    					if ( mutuallyExclusive(scope.params.control.name, scope.params.experiment.name) ){
	    						scope.badSelection = false
	    						return
	    						
	    					} else {
	    						selectionWarning()
	    					}
	    				}
	    			})
	    			
	    			scope.$watch('params.control', function(newval){
	    				if (newval && scope.params.experiment && scope.dataset){
	    						
	    					if ( mutuallyExclusive(scope.params.control.name, scope.params.experiment.name) ){
	    						scope.badSelection = false
	    						return
	    					} else {
	    						selectionWarning()
	    					}
	    				}
	    			})
	    			
	    			var parametersOK = function(){
	    				
	    				var intersecting = !mutuallyExclusive(scope.params.control.name, scope.params.experiment.name)
	    				
	    				var failing = (!scope.params.name || !scope.params.control || !scope.params.experiment || intersecting)
	
	    				return !failing
	    			}
	    			var mutuallyExclusive = function(set1, set2){
	    				
	    				var intersection = scope
	    					.dataset.selections
	    					.intersection({'selection1':set1, 'selection2':set2, 'dimension':'column'})
	    				return (intersection.length == 0)
	    			}
					
					scope.testInit = function(){
						
						if (!parametersOK()){
							alertService.error("Bad analysis parameters selection", "DESeq Analysis Start Error")
							return
						}
						
	    				scope.dataset.analysis.post3({
	    					datasetName:scope.dataset.datasetName,
	    					analysisType:'deseq',
	    					analysisName:scope.params.name,
	    					experiment: scope.params.experiment.name,
	    					control: scope.params.control.name
	    				},{})
	    				
	    			}
	
	    			
	    			
	    		}
	    	}
	    	
	    }])
	    .directive('modalFTest', ['alertService', function(alertService){
	    	
	    	return {
	    		restrict: 'C',
	    		scope: {
	    			dataset : '=heatmapDataset'
	    		},
	    		templateUrl : module.path + 'templates/fTest.tpl.html',
	    		link : function(scope, elements, attributes){
	    			
	    			scope.params = {
	    				name: undefined,
	    				dimension: {
	                        name : 'Rows',
	                        value : 'row'
	                    },
	                    population: undefined,
	    				selection1: undefined,
	    				selection2: undefined,
	    				threshold: undefined,
	    				simulate: undefined,
	    				hypothesis: undefined
	    			}
	    			
	    			scope.options = {
						dimension : [{name : 'Rows', value : 'row'}, {name : 'Columns',value : 'column'}],
	                    simulate: [{name:'True', value:true}, {name:'False', value:false}],
	                    hypothesis: [{name:'Two-sided', value:'two.sided'},{name:'Greater', value:'greater'},{name:'Less', value:'less'}]
	    			}
	    			
	    			var parametersOK = function(){
                    	
	    				var failing = (!scope.params.name || !scope.params.population || 
	    						!scope.params.selection1 || !scope.params.selection2 ||
	    						!scope.params.threshold)
	
	    				return !failing
	    			}
	    			
	    			scope.testInit = function(){
	    				
	    				if (!parametersOK()) {
                            
                            message = "Bad analysis parameters selection";

                            header = "F-Test Start Error";
                            
                            alertService.error(message, header);
                            return
                        }
	    				
	    				var experiments = [];
	
	    				var groups = [];
	    				
	    				var selection_dimension = (scope.params.dimension.value == 'row') ? 'column' : 'row'
	    					
	    				if (scope.dataset.selections[selection_dimension].keys > 1 
	    						|| scope.dataset.selections[selection_dimension].keys < 1){
	    					console.log("Failed T-Test Initialization")
	    					failure({}, "Initialization Failure")
	    					return
	    				}
	    				
	    				
	    				for (selection in scope.dataset.selections[selection_dimension]) {
	    					
	    					if (scope.dataset.selections[selection_dimension][selection].name == scope.params.selection1.name ){
	    						groups.push(scope.dataset.selections[selection_dimension][selection].keys)
	    					}
	    				}
	    				
	    				for (selection in scope.dataset.selections[selection_dimension]) {
	    					if (scope.dataset.selections[selection_dimension][selection].name == scope.params.selection2.name ){
	    						groups.push(scope.dataset.selections[selection_dimension][selection].keys)
	    					}
	    				}
	    				
						var experiments = []
					
						for (population in scope.params.population.keys) {
							
							experiments.push({
								dimension: scope.params.dimension.value,
								groups: groups,
								population: scope.params.population.keys[population],
								threshold: parseFloat(scope.params.threshold)
	    					})
	    					
	    				}
	    				
						var tables = []
						
						for (experiment in experiments){
							
							
							try {
								tables.push(scope.dataset.expression.statistics().contingency(experiments[experiment]) )
							} catch (err) {
								
								failure({}, "Initialization Failure ("+err.message+")")
								return
							}
							
						}
						
						var success = function(data, status, headers, config){
							
							scope.dataset.loadAnalyses()
	                		var message = "Fisher's Test analysis for "
	                			+ scope.params.name + " complete!"
	
	                        var header = "Fisher's Test Analysis"
	
	                        alertService.success(message,header)
	    				}
	    				
	    				var failure = function(data, status, headers, config) {
	                        var message = "Could not perform Fisher's Test analysis. If "
	                            + "problem persists, please contact us.";
	                        var header = "Fisher's Test Problem (Error Code: "
	                            + status
	                            + ")";
	                        alertService.error(message,header);  
	    				}
					
						for (table in tables){
							
							scope.dataset.analysis.post3({
								datasetName:scope.dataset.datasetName,
								analysisType:'fisher',
								analysisName:scope.params.name + "-" + scope.params.population.keys[table]
							}, {
		    					m: tables[table][0].above,
		    					n: tables[table][1].above,
		    					s: tables[table][0].below,
		    					t: tables[table][1].below,
		    					hypothesis: scope.params.hypothesis.value,
		    					simulate: scope.params.simulate.value
		    				})
							
						}
						
	    			}
	    		}
	    	}
	    	
	    }])
	    .directive('modalWilcoxon', ['alertService', function(alertService){
	    	
	    	return {
	    		restrict: 'C',
	    		scope: {
	    			dataset : '=heatmapDataset'
	    		},
	    		templateUrl : module.path + 'templates/wilcoxonTest.tpl.html',
	    		link : function(scope, elements, attributes){
	    			
	    			
	    			scope.params = {
	    				name: undefined,
	    				selection1: undefined,
	    				selection2: undefined,
	    				pair: undefined,
	    				confidentInterval: undefined,
	    				hypothesis: undefined
	    			}
	    			
	    			scope.options = {
	    				pair: [{name:'True', value:true}, {name:'False', value:false}],
	                    confidentInterval: [{name:'True', value:true}, {name:'False', value:false}],
	                    hypothesis: [{name:'Two-sided', value:'two.sided'},{name:'Greater', value:'greater'},{name:'Less', value:'less'}]
	    			}
	    			
	    			var parametersOK = function(){
                    	
	    				var failing = (!scope.params.name || !scope.params.pair || 
	    						!scope.params.selection1 || !scope.params.selection2 ||
	    						!scope.params.confidentInterval || !scope.params.hypothesis)
	
	    				return !failing
	    			}
	    			
	    			scope.testInit = function(){
	    				
	    				if (!parametersOK()) {
	                        
	                        message = "Bad analysis parameters selection";

	                        header = "Wilcoxon Test Start Error";
	                        
	                        alertService.error(message, header);
	                        return
	                    }
	    				
	    				var success = function(data, status, headers, config){
	
							scope.dataset.loadAnalyses()
	                		var message = "Wilcoxon Test analysis for "
	                			+ scope.params.name + " complete!"
	
	                        var header = "Wilcoxon Test Analysis"
	
	                        alertService.success(message,header)
	    				}
	    				
	    				var failure = function(data, status, headers, config) {
	                        var message = "Could not perform Wilcoxon Test analysis. If "
	                            + "problem persists, please contact us.";
	                        var header = "Wilcoxon Test Problem (Error Code: "
	                            + status
	                            + ")";
	                        alertService.error(message,header);  
	    				}
	    				
	    				scope.dataset.analysis.post4({
							datasetName:scope.dataset.datasetName,
							analysisType:'wilcoxon',
							analysisName:scope.params.name,
							first: scope.params.selection1,
	    					second: scope.params.selection2,
	    					pair: scope.params.pair.value,
	    					confidentInterval: scope.params.confidentInterval.value,
	    					hypothesis: scope.params.hypothesis.value
						})
	    			}
	    		}
	    	}
	    	
	    }])
	    .directive('modalTTest',['alertService',
	        function(alertService) { 
	            return {
	                restrict : 'C',
	                scope : {
	                	dataset : '=heatmapDataset'
	                },
	                templateUrl : "/container/view/elements/tTestModalBody",
	                link : function(scope, elems, attrs) {
	                    
	                    scope.params = {
	                            name: undefined,
	                            sampleType: {label:"", url:""},
	                    		pValue: 0.05,
	                    		multitestCorrection: false,
	                    		assumeEqualVariance: false
	                    };
	                    
	                    scope.options = {
	                    		sampleTypes: [{label: "one sample", url:"one_sample_ttest"}, 
	                    		        {label: "two sample", url:"two_sample_ttest"}]
	                    };
	                    
	                    scope.isOneSample = function(){
	                		return scope.params.sampleType.url=='one_sample_ttest';
	                	};
	                	
	                	scope.isTwoSample = function(){
	                		return scope.params.sampleType.url=='two_sample_ttest';
	                	};
	                	
	                	scope.getPostData = function(){
	                		var postRequest = {
	                			name: scope.params.name,
	                			pValue: scope.params.pValue,
	                			multTestCorrection: scope.params.multitestCorrection,
	                			experimentName: scope.params.analysisExperiment.name
	                		};
	                		if(scope.isOneSample()){                                			
	                			postRequest.userMean=scope.params.userMean;
	                		}else{
	                			postRequest.controlName=scope.params.analysisControl.name;
	                		}
	                		if(scope.isTwoSample()){
	                			postRequest.assumeEqualVariance=scope.params.assumeEqualVariance
	                		}
	                		return postRequest;                                		
	                	};  
	                	
	                	var parametersOK = function(){
	                    	
		    				var failing = (!scope.params.name)
		
		    				return !failing
		    			}
	                	
	                    scope.testInit = function(){
	                        
	                    	if (!parametersOK()) {
		                        
		                        message = "Bad analysis parameters selection";

		                        header = "T-Test Start Error";
		                        
		                        alertService.error(message, header);
		                        return
		                    }
	
	                    	scope.dataset.analysis.post({
	                        	datasetName : scope.dataset.datasetName, 
	                            analysisType : scope.params.sampleType.url
	                            
	                    	}, scope.getPostData());
	                    };
	                }
	            };
	    }])
	    .directive('modalHierarchical', ['alertService',
	        function(alertService) {
	
	            return {
	                restrict : 'C',
	                templateUrl : "/container/view/elements/hierarchicalbody",
	                scope : {
	                	dataset : "=heatmapDataset"
	                },
	                link : function(scope, elems, attrs) {
	                    
	                    scope.options = {
	                            metrics : [{name:"Euclidean", value:"euclidean"},
	                                       {name:"Manhattan", value:"manhattan"},
	                                       {name:"Pearson", value:"pearson"}],
	                            linkage : [{name:"Complete", value:'complete'},
	                                       {name:"Average", value:'average'},
	                                       {name:"Single", value:'single'}],
	                            dimensions : [{
	                                            name : 'Rows',
	                                            value : 'row'
	                                        }, {
	                                            name : 'Columns',
	                                            value : 'column'
	                                        }]
	                    };
	                    
	                    scope.params = {
	                            metric : scope.options.metrics[0],
	                            dimension : scope.options.dimensions[1],
	                            linkage : scope.options.linkage[0],
	                            name : undefined
	                    }
	                    
	                    scope.params.selectedMetric = {name:"Euclidean", value:"euclidean"}
	                    
	                    var parametersOK = function(){
	                    	
		    				var failing = (!scope.params.name)
		
		    				return !failing
		    			}
	
	                    scope.testInit = function() {
		                	
		                	if (!parametersOK()) {
		                        
		                        message = "Bad analysis parameters selection";

		                        header = "Hierarchical Start Error";
		                        
		                        alertService.error(message, header);
		                        return
		                    }
	                    	
	                        var analysisData = {
	                        	name : scope.params.name,
	                        	dimension : scope.params.dimension.value,
	                        	metric : scope.params.metric.value,
	                        	linkage : scope.params.linkage.value
	                        };
	                        
	                        scope.dataset.analysis.post({
	                        	datasetName : scope.dataset.datasetName, 
	                            analysisType : 'hcl'
	                            
	                    	}, analysisData);
	
	                    };
	
	                    function resetSelections() {
	                        scope.params = {
	                                metric : scope.options.metrics[0],
	                                dimension : scope.options.dimensions[1],
	                                linkage : scope.options.linkage[0],
	                                name : undefined
	                        }
	                    }
	
	                }
	
	            };
	
	        }])
	        .directive(
	            'modalKmeans',['alertService',
	            function(alertService) {
	
	                return {
	                    restrict : 'C',
	                    scope : {
	                    	dataset : "=heatmapDataset"
	                    },
	                    templateUrl : "/container/view/elements/kMeansBody",
	                    link: function(scope){
	                        
	                        scope.options = {
	                                'dimensions':[{'name': 'Rows', 'value':'row'},
	                                              {'name': 'Columns', 'value':'column'} ],
	                                'clusters':[2, 3, 4, 5, 6, 7, 8],
	                                'metrics':[{'name': 'Euclidean', 'value':'euclidean'} ],
	                                'iterations': [100, 1000]
	                        }
	                        
	                        scope.params = {
	                                'analysisName':'',
	                                'analysisDimension':scope.options.dimensions[0],
	                                'analysisClusters': scope.options.clusters[3],
	                                'analysisMetric':scope.options.metrics[0],
	                                'analysisIterations':scope.options.iterations[0],
	                                'analysisConvergence': 0
	                        }
	                        
	                        var parametersOK = function(){
		                    	
			    				var failing = (!scope.params.analysisName)
			
			    				return !failing
			    			}
			                	
			                	
	                        
	                        scope.testInit = function(){
	                        	
	                        	if (!parametersOK()) {
			                        
			                        message = "Bad analysis parameters selection";

			                        header = "K-Means Clustering Start Error";
			                        
			                        alertService.error(message, header);
			                        return
			                    }
	                            	                            
	                            var analysisData = {
	                            	name : scope.params.analysisName,
	                            	dimension : scope.params.analysisDimension,
	                            	clusters : scope.params.analysisClusters,
	                            	metric : scope.params.analysisMetric,
	                            	iterations : scope.params.analysisIterations,
	                            	convergence : scope.params.analysisConvergence
	                            };
	                            
	                            scope.dataset.analysis.postf({
	
		                            datasetName : scope.dataset.datasetName,
		                            analysisType : 'kmeans',
		                            analysisName : analysisData.name,
		                            analysisParams : "dimension=" + analysisData.dimension.value
		                                + ",k=" + analysisData.clusters
		                                + ",metric=" + analysisData.metric.value
		                                + ",iterations=" + analysisData.iterations
		                                + ",convergence=" + analysisData.convergence
	                            }, {});
	                        }
	                    }
	
	                };
	
	            }])
	            
	            .directive('modalLimma', [ "alertService",
	                function(alertService) {
	
	                    return {
	                        restrict : 'C',
	                        templateUrl : "/container/view/elements/limmaBody",
	                        scope : {dataset : "=heatmapDataset"},
	                        link : function(scope, elems, attrs) {
	
	                            scope.available = {
	                            		
		                            'dimensions' : [{
		                                name : "Column",
		                                value : "column"
		                            }],
		                            'species' : [{
			                                name : "Human",
			                                value : "human"
			                            },{
			                                name : "Rat",
			                                value : "rat"
			                            },{
			                                name : "Mouse",
			                                value : "mouse"
		                            }],
		                            'goType':[{
			                                name : "Biological Process",
			                                value : "BP"
			                            },{
			                                name : "Molecular Function",
			                                value : "MF"
			                            },{
			                                name : "Cellular Component",
			                                value : "CC"
		                            }],
		                            'testType':[{
		                                name : "Fisher",
		                                value : "Fisher"
		                            },{
		                                name : "Kolmogorov-Smirnov",
		                                value : "KS test"
		                            }],
	                            }
	                            
	                            scope.params = {
	                                	
	                        		name : undefined,
	                        		dimension : scope.available.dimensions[0],
	                        		experiment : undefined,
	                        		control : undefined,
	                        		species : scope.available.species[0],
	                        		goType: scope.available.goType[0],
	                        		testType: scope.available.testType[0],
	                        		goAnalysis: true
	                        	
	                        	};
	                            
	                            var selectionWarning = function(){
	                				alertService.error("Cannot start LIMMA with intersecting selection parameters", "LIMMA Parameters problem")
	                				scope.badSelection = true;
	                			}
	                			
	                			scope.$watch('params.experiment', function(newval){
	                				
	                				if (newval && scope.params.control && scope.dataset){
	                					
	                					if ( mutuallyExclusive(scope.params.control.name, scope.params.experiment.name) ){
	                						scope.badSelection = false
	                						return
	                						
	                					} else {
	                						selectionWarning()
	                					}
	                				}
	                			})
	                			
	                			scope.$watch('params.control', function(newval){
	                				if (newval && scope.params.experiment && scope.dataset){
	                						
	                					if ( mutuallyExclusive(scope.params.control.name, scope.params.experiment.name) ){
	                						scope.badSelection = false
	                						return
	                					} else {
	                						selectionWarning()
	                					}
	                				}
	                			})
	                			
	                			var parametersOK = function(){
	                				
	                				var intersecting = !mutuallyExclusive(scope.params.control.name, scope.params.experiment.name)
	                				
	                				var failing = (!scope.params.name || !scope.params.control || !scope.params.experiment || intersecting)
	
	                				return !failing
	                			}
	                			var mutuallyExclusive = function(set1, set2){
	                				
	                				var intersection = scope
	                					.dataset.selections
	                					.intersection({'selection1':set1, 'selection2':set2, 'dimension':'column'})
	            					
	                				return (intersection.length == 0)
	                			}
	
	                            scope.testInit = function() {
	                            	
	                            	if (!parametersOK()){
	            						alertService.error("Bad analysis parameters selection", "LIMMA Analysis Start Error")
	            						return
	            					}
	                  
	                                var analysisData = {
	                            		analysisType: 'limma',
	                                	datasetName: scope.dataset.datasetName,
	                                	analysisName : scope.params.name,
	                                	
	                                	dimension : scope.params.dimension.value,
	                                	experiment : scope.params.experiment.name,
	                                	control : scope.params.control.name,
	                                	species : (scope.params.goAnalysis) ? scope.params.species.value : undefined,
	                                	go : (scope.params.goAnalysis) ? scope.params.goType.value : undefined,
	                                	test : (scope.params.goAnalysis) ? scope.params.testType.value : undefined
	                                };
	                                
	                                scope.dataset.analysis.post3(analysisData, {
	                                	
	                                });                                              
	                                
	
	
	                                scope.params.name = undefined;
	
	                            };
	
	                        }
	
	                    };
	
	                }])
	                
	
		return module
	
	
		
	});
	
})();
