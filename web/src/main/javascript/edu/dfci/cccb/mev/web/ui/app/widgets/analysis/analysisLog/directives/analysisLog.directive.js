define(["ng"], function(ng){
	var AnalysisLogDirective = function AnalysisLogDirective(AnalysisEventBus, alertService){
		return {
			scope: {
				showAlerts: "@"
			},
			restrict: "AE",
			templateUrl: "app/widgets/analysis/analysisLog/directives/analysisLog.tpl.html",
			link: function(scope, elem, attrs, ctrl){
				
				function newLogline(message, type){
					return {
						message: message,
						type: type
					};
				}
				
				scope.vm={
						log: [newLogline("Session log initialized", "info")]
				};
				
				AnalysisEventBus.onAnalysisStarted(scope, function(type, name, eventData){
					console.debug("onAnalysisStarted handler", type, name, eventData);
					var message="Started " + type + " analysis " + name;;
					scope.vm.log.unshift(newLogline(message, "info"));
					if(scope.showAlerts)
						alertService.info(message,type);
				});
				
				AnalysisEventBus.onAnalysisSuccess(scope, function(type, name, data){
					console.debug("onAnalysisSuccess handler", type, name, data);
					var message = "Completed " + type + " analysis " + name;
					scope.vm.log.unshift(newLogline(message, "success"));
					if(scope.showAlerts)
						alertService.success(message,type);
				});
				
				AnalysisEventBus.onAnalysisFailure(scope, function(type, params){
					console.debug("onAnalysisFailure handler", type, params);
					var message = "Error while processing " + type + " analysis " + params.name;
					scope.vm.log.unshift(newLogline(message, "error"));
					if(scope.showAlerts)
						alertService.error(message, type);
				});
			}
		};
	};
	AnalysisLogDirective.$inject=["AnalysisEventBus", "alertService"];
	return AnalysisLogDirective;
});