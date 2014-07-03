define(['angular', 'alertservice/AlertService'], function(angular){
	
	return angular.module('Mev.AnalysisModalCollection', ['Mev.AlertService'])
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
                    
                    scope.testInit = function(){
                        
                        var message = "Starting ANOVA for "
                            + scope.params.name + " analysis.";

                        var header = "ANOVA";
                        
                        if (scope.params.selections.length < 2) {
                            
                            message = "Can't start ANOVA for "
                                + scope.params.name + " with less than two groups.";

                            header = "ANOVA";
                            
                            alertService.info(message,header);
                            return
                        }
                        
                        alertService.info(message,header);
                        
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
                        .postf(analysisData, JSON.stringify(scope.params.selections),
	                        function(data, status, headers, config) {
	                            
                        		scope.dataset.loadAnalyses();
                        		
	                            var message = "ANOVA for "
	                            	+ scope.params.name + " complete!";
	
	                            var header = "ANOVA";
	                                         
	                            alertService.success(message,header);
	                                    
	                         }, function(data, status, headers, config) {
	                            
	                            var message = "Could not perform ANOVA. If "
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
                    scope.testInit = function(){
                        

                    	scope.dataset.analysis.post({
                        	datasetName : scope.dataset.datasetName, 
                            analysisType : scope.params.sampleType.url
                            
                    	}, scope.getPostData(), 
                        function(data, status, headers, config) {
                    	
                    		scope.dataset.loadAnalyses()
                    		var message = "t-Test analysis for "
                    			+ scope.params.name + " complete!";

                            var header = "t-Test Analysis";
                                         
                            alertService.success(message,header);
                                    
                        },
                        
                        function(data, status, headers, config) {
                            
                            var message = "Could not perform t-Test. If "
                                + "problem persists, please contact us.";
                            var header = "Clustering Problem (Error Code: "
                                + status
                                + ")";
                            alertService.error(message,header);                                            
                            
                        });
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

                    scope.testInit = function() {

                        var message = "Started clustering analysis for "
                            + scope.params.name;

                        var header = "Hierarchical Clustering Analysis";
                         
                        alertService.info(message,header);

                        var analysisData = {
                        	name : scope.params.name,
                        	dimension : scope.params.dimension.value,
                        	metric : scope.params.metric.value,
                        	linkage : scope.params.linkage.value
                        };
                        
//                        scope.dataset.analysis.postf({
//                            datasetName : scope.dataset.datasetName,
//                            analysisType : 'hcl',
//                            analysisName : analysisData.name,
//                            analysisParams : analysisData.dimension + ','
//                                + analysisData.metric + ','
//                                + analysisData.linkage
//                        }, {},
                        scope.dataset.analysis.post({
                        	datasetName : scope.dataset.datasetName, 
                            analysisType : 'hcl'
                            
                    	}, analysisData, 
                        function(data, status, headers, config) {
                            
                        	scope.dataset.loadAnalyses();
                        	
                            var message = "Clustering analysis for "
                                + scope.params.name + " complete!";

                            var header = "Hierarchical Clustering Analysis";
                             
                            alertService.success(message,header);
                                                                
                        },
                        function(data, status, headers, config) {
                            
                            var message = "Could not perform clustering. If "
                                + "problem persists, please contact us.";
                            var header = "Clustering Problem (Error Code: "
                                + status
                                + ")";
                            alertService.error(message,header);
                            
                        });

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
                        
                        scope.testInit = function(){
                            
                            var message = "Started K-Means analysis for "
                                + scope.params.analysisName;

                            var header = "K-Means Clustering Analysis";
                             
                            alertService.info(message,header);
                            
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
                            }, {},
                            
                            function(data, status, headers, config) {
                                
                            	scope.dataset.loadAnalyses();
                            	
                                var message = "K-Means analysis for "
                                    + scope.params.analysisName + " complete!";

                                var header = "K-Means Clustering Analysis";
                                 
                                alertService.success(message,header);
	                            
	                        },
                                            
                            function(data, status, headers, config) {
                                        
                                var message = "Could not perform k-means clustering. If "
                                    + "problem persists, please contact us.";
                                var header = "Clustering Problem (Error Code: "
                                    + status
                                    + ")";
                                alertService.error(message,header);
                                
                            });
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

                        	
                        	scope.params = {
                        	
                        		name : undefined,
                        		dimension : [{
	                                name : "Column",
	                                value : "column"
	                            }],
                        		experiment : undefined,
                        		control : undefined,
                        		species : undefined,
                        		goType: undefined,
                        		testType: undefined
                        	
                        	};
                        	
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
		                                name : "BP",
		                                value : "BP"
		                            },{
		                                name : "MF",
		                                value : "MF"
		                            },{
		                                name : "CC",
		                                value : "CC"
	                            }],
	                            'testType':[{
	                                name : "Fisher",
	                                value : "Fisher test"
	                            },{
	                                name : "KS",
	                                value : "KS test"
	                            }],
                            }

                            scope.testInit = function() {
                                

                                var message = "Started limma analysis for "
                                    + scope.params.name;

                                var header = "LIMMA Analysis";
                                 
                                alertService.info(message,header);

                                var analysisData = {
                                	name: scope.params.name,
                                	experiment : scope.params.experiment.name,
                                	control : scope.params.control.name,
                                	species : scope.params.species.value,
                                	goType : scope.params.goType.value,
                                	testType : scope.params.testType.value
                                };
                                
                                scope.dataset.analysis.post({
                                	analysisType: 'limma/' + analysisData.name
                                    
                                }, {
                                	'dimension' : 'column',
                                	'experiment' : analysisData.experiment,
                                	'control' : analysisData.control,
                                	'species' : analysisData.species,
                                	'go' : analysisData.goType,
                                	'test' : analysisData.testType
                                },
                                function(data, status, headers, config) {
                                    
                                	scope.dataset.loadAnalyses();
                                	
                                    var message = "Completed limma analysis for "
                                        + scope.analysisName;

                                    var header = "LIMMA Analysis Complete";
                                     
                                    alertService.success(message,header);
                                    
                                },
                                function(data, status, headers, config) {
                                    var message = "Error on limma analysis for "
                                        + scope.analysisName;

                                    var header = "LIMMA Analysis Problem (Error Code: "
                                        + status
                                        + ")";
                                     
                                    alertService.error(message,header);
                                });

                                resetSelections();                                                    
                                
                               

                            };

                            function resetSelections() {
                            	scope.params = {
                                    	
                            		name : undefined,
                            		dimension : [{
    	                                name : "Column",
    	                                value : "column"
    	                            }],
                            		experiment : undefined,
                            		control : undefined
                            	
                            	};
                            };

                        }

                    };

                }])


	
});