define(["angular"], function(angular){
	'use strict';
	
	var AnalysisEventBus = function($rootScope){
				
		
		var MSG_ANALYSIS_STARTED="event:analysis:start";		
		this.analysisStarted = function(descriptor, params){
			console.debug("broadcast AnalysisStarted");
			$rootScope.$broadcast(MSG_ANALYSIS_STARTED, {
				analysisType: descriptor.analysisType,
				datasetName: descriptor.datasetName,
				params: params
			});
		};
		
		this.onAnalysisStarted = function($scope, handler){
			console.debug("register onAnalysisStarted");
			$scope.$on(MSG_ANALYSIS_STARTED, function($event, data){
				console.debug("recieved onAnalysisStarted", $event, data);
				handler(data.analysisType, data.params);
			});
		};
		
		var MSG_ANALYSIS_SUCCESS="event:analysis:success";				
		this.analysisSucceeded=function(descriptor, params){
			$rootScope.$broadcast(MSG_ANALYSIS_SUCCESS, {
				analysisType: descriptor.analysisType,
				datasetName: descriptor.datasetName,
				params: params
			});		
		};
		this.onAnalysisSuccess = function($scope, handler){			
			$scope.$on(MSG_ANALYSIS_SUCCESS, function($event, analysis){
				handler(analysis.analysisType, analysis.params);
			});
		};
		
		var MSG_ANALYSIS_FAILURE="event:analysis:failure";				
		this.analysisFailed=function(descriptor, params){
			$rootScope.$broadcast(MSG_ANALYSIS_FAILURE, {
				analysisType: descriptor.analysisType,
				datasetName: descriptor.datasetName,
				params: params
			});
		};
		this.onAnalysisFailure = function($scope, handler){			
			$scope.$on(MSG_ANALYSIS_FAILURE, function($event, analysis){
				handler(analysis.analysisType, analysis.params);
			});
		};
		
		var MSG_ANALYSIS_LOADED_ALL="event:analysis:all";
		this.analysisLoadedAll = function(){
			$rootScope.$broadcast(MSG_ANALYSIS_LOADED_ALL);
		};
		this.onAnalysisLoadedAll = function($scope, handler){
			$scope.$on(MSG_ANALYSIS_LOADED_ALL, function($event){
				handler();
			});
		}
	};
	
	AnalysisEventBus.$inject=["$rootScope"];
	return AnalysisEventBus;
});