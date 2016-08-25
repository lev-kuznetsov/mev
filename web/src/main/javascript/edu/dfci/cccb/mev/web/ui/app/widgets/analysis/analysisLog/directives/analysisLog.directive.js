define(["./analysisLog.tpl.html"], function(template){
	var AnalysisLogDirective = function AnalysisLogDirective(AnalysisEventBus, alertService){
		return {
			scope: {
				showAlerts: "@"
			},
			restrict: "AE",
			template: template,
			link: function(scope, elem, attrs, ctrl){
				
				function newLogline(message, type){
					return {
						message: message,
						type: type
					};
				}
				function log(message, level){
					scope.vm.log.unshift(newLogline(message, level));
				}
				scope.vm={
						log: [newLogline("Session log initialized", "info")]
				};
				
				AnalysisEventBus.onAnalysisStarted(scope, function(type, name, eventData){
					console.debug("onAnalysisStarted handler", type, name, eventData);
					var message="Started " + type + " analysis " + name;;
					log(message, "info");
					if(scope.showAlerts)
						alertService.info(message,type);
				});
				
				AnalysisEventBus.onAnalysisSuccess(scope, function(type, name, data){
					console.debug("onAnalysisSuccess handler", type, name, data);
					var message = "Completed " + type + " analysis " + name;					
					log(message, "success");
					if(scope.showAlerts)
						alertService.success(message,type);
				});
				
				AnalysisEventBus.onAnalysisFailure(scope, function(type, params){
					console.debug("onAnalysisFailure handler", type, params);
					var message = "Error while processing " + type + " analysis " + params.name;
					log(message, "error");
					if(scope.showAlerts)
						alertService.error(message, type);
				});
				scope.$on("ui:analysisLog.append", function($event, level, message){
					log(message, level);
				});
			}
		};
	};
	AnalysisLogDirective.$name="analysisLog";
	AnalysisLogDirective.$inject=["mevAnalysisEventBus", "alertService"];
	AnalysisLogDirective.$provider="directive";
	return AnalysisLogDirective;
});