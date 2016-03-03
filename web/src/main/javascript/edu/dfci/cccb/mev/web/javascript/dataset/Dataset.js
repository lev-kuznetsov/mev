define(['angular', 
        './lib/DatasetClass',
        './lib/loadAnalyses',
        './lib/setSelections',
        './lib/resetSelections',        
        'api/Api'], 
function(angular, DatasetClass,loadAnalyses, setSelections, resetSelections){ "use strict";
	
	return angular.module('Mev.Dataset', ['Mev.Api'])
	.factory('DatasetFactory', ['AnalysisResourceService', 'SelectionResourceService', "$q", "$http", '$rootScope', 'AnalysisEventBus', "DashboardItems", "mevAnnotationRepository",
	 function(AnalysisResourceService, SelectionResourceService, $q, $http, $rootScope, analysisEventBus, DashboardItems, MevAnnotationRepository){
	    
	    //DatasetFactory :: [String], [DatasetResponseObj] -> [Dataset]
	    //  Function that takes dataset name and dataset response object and returns
	    //  new dataset.
		return function(datasetName, datasetResponseObj){
		    
		    
				var dataset = new DatasetClass(datasetName, datasetResponseObj, $http, $rootScope);				
				
				dataset.analysis = AnalysisResourceService;
				console.debug("api:AnalysisResourceService", AnalysisResourceService, dataset.analysis);
				dataset.selection = SelectionResourceService;

				dataset.$q = $q;				
				dataset.analysisEventBus = analysisEventBus;
				analysisEventBus.onAnalysisSuccess($rootScope, function(analysis){
//					if(!dataset.analyses)
//						dataset.analyses = [];
//					dataset.analyses.push(analysis);
				});
				dataset.dashboardItems = new DashboardItems();
				dataset.loadAnalyses = loadAnalyses;
				dataset.setSelections = setSelections;
				dataset.resetSelections = resetSelections;
				

				dataset.getAnnotations=function(dimension){
					if(!this._annotations)
						this._annotations={};				
					if(!this._annotations[dimension])
						this._annotations[dimension] = new MevAnnotationRepository(dimension);
					return this._annotations[dimension];
				};

				return dataset;
				
			};
	}]);
});