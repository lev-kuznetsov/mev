define(["lodash", "./SelectParam.tpl.html"], function(_, template){
	function mevSelectParamDirective($q){
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

				function setOptions(){
					var theOptions = scope.param.options();
					if(_.isFunction(theOptions.then)){
						theOptions.then(function(options){
							scope.param.optionsx = options;
						});
					}else{
						scope.param.optionsx = theOptions;
					}
				}				
				setOptions();
				scope.$on("openRefine:loadedAnnotations:"+scope.param.dimension, function(){
					console.debug("loadedProject 2");
					setOptions();
				});
				scope.isOptionsArray = spec.display ? false : true;
			}
		};
	}
	mevSelectParamDirective.$inject=["$q"];
	mevSelectParamDirective.$name="mevSelectParamDirective";
	return mevSelectParamDirective;
});