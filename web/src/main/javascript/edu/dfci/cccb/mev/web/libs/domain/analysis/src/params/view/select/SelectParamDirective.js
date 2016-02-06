define(["lodash", "./SelectParam.tpl.html"], function(_, template){
	function mevSelectParamDirective(){
		return {
			restrict: "EAC",
			template: template,
			scope: {
				param: "=mevParam"
			},
			controller: ["$scope", function(scope){
				var spec = scope.param;
				scope.initial = spec.value;
			}],
			link: function(scope){
				var spec = scope.param;
				scope.isOptionsArray = spec.display ? false : true;

			}
		}
	}
	mevSelectParamDirective.$inject=[];
	mevSelectParamDirective.$name="mevSelectParamDirective";
	return mevSelectParamDirective;
})