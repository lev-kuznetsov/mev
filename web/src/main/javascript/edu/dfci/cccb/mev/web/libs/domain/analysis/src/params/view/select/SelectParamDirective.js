define(["lodash", "./SelectParam.tpl.html"], function(_, template){
	function mevSelectParamDirective($q, mevContext){
		return {
			restrict: "EAC",
			template: template,
			scope: {
				param: "=mevParam"
			},
			controller: ["$scope", function(scope){
				var spec = scope.param;
				scope.initial = spec.value;
				function decorateOptions(options){
					scope.param.optionsx = _.clone(options);
					if((mevContext.getLevel()===scope.param.allowAll || scope.param.allowAll===true)  && !_.find(scope.param.optionsx, function(item){
							return item==="all" || (scope.param.display && item[scope.param.display] === "all");
					})){
						var all;
						if(scope.param.display){							
							all = {};
							all[scope.param.display] = "all";
							if(scope.param.bound) all[scope.param.bound] = undefined;
						}else{
							all = "all";
						}
						scope.param.optionsx.unshift(all);
					}							
					scope.setValue(options);
				}
				scope.setOptions = function setOptions(){	
					var theOptions = scope.param.getOptions();
					if(_.isFunction(theOptions.then)){
						theOptions.then(function(options){
							scope.param.optionsx = options;
						});
					}else{						
						decorateOptions(theOptions);
						scope.setValue(theOptions);
						scope.$watch(function(){return scope.param.getOptions();}, function(newv, oldv){
							if(!newv) return;
							if(_.isEqual(newv, oldv)) return;
							if(newv === oldv) return;
							if(newv.length === 0 && oldv.length === 0) return;							
							decorateOptions(newv);
						}, true);
					}

				};
				scope.setValue = function setValue(options){
					if(scope.param.setValue){
						scope.param.setValue(options);
					}else if(scope.param.required && options.length===1){
						scope.param.value = options[0];
					}
					// else 
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
	mevSelectParamDirective.$inject=["$q", "mevContext"];
	mevSelectParamDirective.$name="mevSelectParamDirective";
	return mevSelectParamDirective;
});