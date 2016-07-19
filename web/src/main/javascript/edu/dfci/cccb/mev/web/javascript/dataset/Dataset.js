define(['angular',
        './lib/DatasetClass',
        './lib/loadAnalyses',
        './lib/setSelections',
        './lib/resetSelections',
        'api/Api',
		'mev-domain-common'],
function(angular, DatasetClass,loadAnalyses, setSelections, resetSelections){ "use strict";
	
	return angular.module('Mev.Dataset', ['Mev.Api'])
	.factory('DatasetFactory', ['mevAnalysisRest', 'SelectionResourceService', "$q", "$http", '$rootScope', 'mevAnalysisEventBus', "DashboardItems",
		"mevAnnotationRepository", "DatasetResourceService", "mevDb", "mevSettings",
	 function(AnalysisResourceService, SelectionResourceService, $q, $http, $rootScope, analysisEventBus, DashboardItems, 
	 	MevAnnotationRepository, DatasetResourceService, mevDb, mevSettings){
	    
	    //DatasetFactory :: [String], [DatasetResponseObj] -> [Dataset]
	    //  Function that takes dataset name and dataset response object and returns
	    //  new dataset.
		return function(datasetName, datasetResponseObj){
		    
		    
				var dataset = new DatasetClass(datasetName, datasetResponseObj, $http, $rootScope, mevDb, mevSettings);


				dataset.analysis = AnalysisResourceService;
				console.debug("api:AnalysisResourceService", AnalysisResourceService, dataset.analysis);
				dataset.selection = SelectionResourceService;
				dataset.deleteSelection = function(dimension, name){
					_.remove(dataset[dimension].selections, function(selection){
						return name === selection.name;
					});
					dataset.selection.delete({datasetName: dataset.id, dimension: dimension, selectionName: name}).$promise.then(function(){
						dataset.resetSelections(dimension);
					});
				};

				dataset.mevDb = mevDb;
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
				dataset.subset = DatasetResourceService.subset;
				dataset.getAll = DatasetResourceService.getAll;
				dataset.getIsActive = function(){
					var _self = this;
					return DatasetResourceService.getAll().$promise
						.then(function(datasetNames){
							return datasetNames.find(function(dataseName){
								return dataseName===dataset.id;
							})
						});
				};

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