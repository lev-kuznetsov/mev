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

				scope.setOptions = function setOptions(){	
					var theOptions = scope.param.options();
					if(_.isFunction(theOptions.then)){
						theOptions.then(function(options){
							scope.param.optionsx = options;
						});
					}else{						
						scope.param.optionsx = theOptions;
						scope.$watch(function(){return scope.param.options();}, function(newv, oldv){
							if(_.isEqual(newv, oldv)) return;
							if(newv === oldv) return;
							if(newv.length === 0 && oldv.length === 0) return;
							scope.param.optionsx = newv;
							scope.setValue(newv);
						}, true);						
						scope.setValue(theOptions);
					}
				};
				scope.setValue = function setValue(options){
					if(scope.param.setValue){
						scope.param.setValue(options);
					}
				};
				scope.registerEventListeners = function registerEventListeners(){
					if(scope.param.refreshListeners){
						scope.param.refreshListeners.map(function(item){
							scope.$on(item, function(){					
								scope.setOptions();
							});
						});
					}
				};

				
				scope.isOptionsArray = spec.display ? false : true;
				scope.setOptions();						
				scope.registerEventListeners();			

			}],
			link: function(scope){				
				
			}
		};
	}
	mevSelectParamDirective.$inject=["$q"];
	mevSelectParamDirective.$name="mevSelectParamDirective";
	return mevSelectParamDirective;
});