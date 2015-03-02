define(["ng"], function(ng){
	var AnalysisLogDirective = function AnalysisLogDirective(AnalysisEventBus, alertService){
		return {
			scope: {
				showAlerts: "@"
			},
			restrict: "AE",
			templateUrl: "app/widgets/analysis/analysisLog/directives/analysisLog.tpl.html",
			link: function(scope, elem, attrs, ctrl){
				
				scope.vm={
						log: ["initialized"]
				};
				
				AnalysisEventBus.onAnalysisStarted(scope, function(type, params){
					console.debug("onAnalysisStarted handler", type, params);
					var message="Started " + type + " analysis " + params.name + " with params " + JSON.stringify(params);
					scope.vm.log.unshift(message);
					if(scope.showAlerts)
						alertService.info(message,type);
				});
				
				AnalysisEventBus.onAnalysisSuccess(scope, function(type, params){
					console.debug("onAnalysisSuccess handler", type, params);
					var message = "Completed " + type + " analysis " + params.name;
					scope.vm.log.unshift(message);
					if(scope.showAlerts)
						alertService.success(message,type);
				});
				
				AnalysisEventBus.onAnalysisFailure(scope, function(type, params){
					console.debug("onAnalysisFailure handler", type, params);
					var message = "Error while processing " + type + " analysis " + params.name;
					scope.vm.log.unshift(message);
					if(scope.showAlerts)
						alertService.error(message,type);
				});
			}
		};
	};
	AnalysisLogDirective.$inject=["AnalysisEventBus", "alertService"];
	return AnalysisLogDirective;
});