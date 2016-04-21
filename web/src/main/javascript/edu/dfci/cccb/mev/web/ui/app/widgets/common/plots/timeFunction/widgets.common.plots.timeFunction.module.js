define(["ng", "lodash", "./healthVis/healthvis", "./healthVis/healthVis_survival"],
function(ng, _, healthVis, healthVisSurvival){
	var module=ng.module("mui.widgets.common.plots.timeFunction", []);
	module.directive("timeFunctionPlot", function(){
		return {
			restrict: "E",
			scope: {
				analysis: "=",
				project: "=",
				isItOpen: "@"
			},
			templateUrl: "app/widgets/common/plots/timeFunction/timeFunctionPlot.tpl.html",
			link: function(scope, elem, attrs){
				var healthVisSurvivalPlot = new healthVisSurvival();
				var maxDomain = _.maxBy(scope.analysis.params.input, 'time').time;
				var experiment = scope.analysis.result.plot.experiment;
				var control = scope.analysis.result.plot.control;
				healthVisParams = {
					"csort" : {},
					"cnames" : [],
					"vars" : [],
					"menutype" : [],
					"daymax" : maxDomain * 1.10,
					"linecol" : [scope.analysis.params.experiment.properties.selectionColor,scope.analysis.params.control.properties.selectionColor],
					"group_names" : [scope.analysis.params.experimentName,scope.analysis.params.controlName],
					"data" : [experiment, control]
				};
				
				healthVisSurvivalPlot.init("#survival-function-plot", healthVisParams);
				scope.plotData = healthVisSurvivalPlot.visualize();	
			}
		};
	});
	return module;
});