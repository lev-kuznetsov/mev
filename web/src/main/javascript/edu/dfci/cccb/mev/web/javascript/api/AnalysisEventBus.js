define(["angular"], function(angular){
	'use strict';
	
	var AnalysisEventBus = function($rootScope, mevAnalysisTypes){
				
		
		function formatAnalysisEventData(descriptor, params, response){
			var eventData = {
					analysisName: descriptor.analysisName || params.analysisName || params.name || response.name
			};
			angular.extend(eventData, descriptor);
			angular.extend(eventData, params);
			var analysisType = mevAnalysisTypes.get(descriptor.analysisType || params.analysisType || response.type);
			if(analysisType && _.isFunction(analysisType.modelDecorator))
				analysisType.modelDecorator(response);
			if(analysisType && _.isFunction(analysisType.onSuccess))
				analysisType.onSuccess(response);
			eventData.response=response;
			return eventData;
		}
		function raiseEvent(eventName, descriptor, params, response){
			var data = formatAnalysisEventData(descriptor, params, response);			
			$rootScope.$broadcast(eventName, data);
			console.debug("broadcast "+eventName, data);
		}
		function registerHandler(eventName, $scope, handler){
			console.debug("registered "+eventName);
			$scope.$on(eventName, function($event, data){
				console.debug("recieved "+eventName, $event, data);
				var type = data.analysisType;
				if(!type && data.response) 
					type = data.response.type;
				handler(type, data.analysisName, data);
			});
		}
		
		var MSG_ANALYSIS_STARTED="event:analysis:start";		
		this.analysisStarted = raiseEvent.bind(this, MSG_ANALYSIS_STARTED);		
		this.onAnalysisStarted = registerHandler.bind(this, MSG_ANALYSIS_STARTED);
		
		var MSG_ANALYSIS_SUCCESS="event:analysis:success";				
		this.analysisSucceeded = raiseEvent.bind(this, MSG_ANALYSIS_SUCCESS);
		this.onAnalysisSuccess = registerHandler.bind(this, MSG_ANALYSIS_SUCCESS);
		
		var MSG_ANALYSIS_FAILURE="event:analysis:failure";				
		this.analysisFailed = raiseEvent.bind(this, MSG_ANALYSIS_FAILURE);
		this.onAnalysisFailure  = registerHandler.bind(this, MSG_ANALYSIS_FAILURE);
		
		var MSG_ANALYSIS_LOADED_ALL="event:analysis:all";
		this.analysisLoadedAll = function(analyses){
			analyses.forEach(function(analysis){
				var type;
				if(analysis.params)
					type = analysis.params.analysisType || analysis.params.type
				else
					type = analysis.type
				var analysisType = mevAnalysisTypes.get(type);
				if(analysisType && _.isFunction(analysisType.modelDecorator))
					analysisType.modelDecorator(analysis);
			})
			$rootScope.$broadcast(MSG_ANALYSIS_LOADED_ALL, analyses);
		};
		this.onAnalysisLoadedAll = function($scope, handler){
			$scope.$on(MSG_ANALYSIS_LOADED_ALL, function($event, analyses){
				handler($event, analyses);
			});
		}
	};

	AnalysisEventBus.$inject=["$rootScope", "mevAnalysisTypes"];
	return AnalysisEventBus;
});