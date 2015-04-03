define(["ng", "./timeFunction/widgets.common.plots.timeFunction.module"],
function(ng){
	var module=ng.module("mui.widgets.common.plots", ["mui.widgets.common.plots.timeFunction"]);
	module.directive("timeFunctionPlot", function(){
		return {
			restrict: "E",
			scope: {
				analysis: "=",
				project: "=",
				isItOpen: "@"
			},
			template: "app/widgets/common/plots/timeFunction/timeFunctionPlot.tpl.html",
			link: function(scope, elem, attrs){
				
			}
		};
	});
	return module;
});