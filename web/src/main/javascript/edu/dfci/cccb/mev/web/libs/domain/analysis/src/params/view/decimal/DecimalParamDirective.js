define(["mev-analysis", "./DecimalParam.tpl.html", "../label/ParamLabelDirective"], function(mod, template){
	function mevDecimalParamDirective(){
		return {
			restrict: "EAC",
			template: template,
			scope: {
				param: "=mevParam"
			},
			controller: ["$scope", function(scope){
				this.param = scope.param;
				this.step = 0.01;
				if(this.param.precision)
					this.step = Math.pow(0.1, this.param.precision);
			}],
			controllerAs: "vm"
		}
	}
	mevDecimalParamDirective.$inject=[];
	mevDecimalParamDirective.$name="mevDecimalParamDirective";
	return mevDecimalParamDirective;
})