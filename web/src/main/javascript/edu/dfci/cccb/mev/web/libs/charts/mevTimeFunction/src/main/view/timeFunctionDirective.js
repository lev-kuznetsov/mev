define(["lodash", "./healthVis/healthvis", "./healthVis/healthVis_survival", "./timeFunctionPlot.tpl.html"],
function(_, healthVis, healthVisSurvival, template){
	var directive = function(){
		return {
			restrict: "E",
			scope: {
				analysis: "=",
				project: "=",
				isItOpen: "@"
			},
			template: template,
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
	};
	directive.$name="mevTimeFunctionPlot";
	directive.$inject=[];
	directive.$provider="directive";
	return directive;
});